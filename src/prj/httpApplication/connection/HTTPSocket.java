package prj.httpApplication.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prj.cyclo.Agent;
import prj.httpparser.httpparser.HTTPRequestParser;
import prj.httpparser.httpparser.HTTPParserListener;
import prj.httpparser.httpparser.RawHTTPRequest;
import prj.httpparser.utils.EventSource;

import java.io.IOException;
import java.net.Socket;

public class HTTPSocket extends EventSource<HTTPSocketListener>
{
    private Socket _socket;
    private HTTPRequestParser _parser;
    private Agent _agent;
    private final Logger _logger;
    private boolean _connected;
    private HTTPParserListener _httpParserListener = new HTTPParserListener()
    {
        @Override
        public void onHttpRequest(RawHTTPRequest request)
        {
            fireRequestArrivedListener(request);
        }

        @Override
        public void onHttpRequestError()
        {
            _logger.info("HTTP Request Error");
            try
            {
                _agent.send(_socket, "Bad Request");
            }
            catch (IOException e)
            {
                _logger.debug("exception while sending/closing socket ", e);
            }
            close();
        }
    };

    public HTTPSocket(Agent agent, Socket socket, HTTPRequestParser parser)
    {
        _agent = agent;
        _socket = socket;
        _connected = true;
        _parser = parser;
        _parser.addListener(_httpParserListener);
        _logger = LoggerFactory.getLogger(HTTPSocket.class.getSimpleName());
    }

    public void parse(String input)
    {
        _parser.parse(input);
    }

    public void send(String response)
    {
        try
        {
            _agent.send(_socket, response);
        }
        catch (IOException e)
        {
            close();
            _logger.error("sending to socket failed", e);
        }
    }

    public void close()
    {
        if (_connected)
        {
            _connected = false;
            _agent.close(_socket);
            fireSocketClosedListener();
        }
    }

    private void fireRequestArrivedListener(RawHTTPRequest request)
    {
        for (HTTPSocketListener l : _listeners)
        {
            l.onRequestArrived(request);
        }
    }

    private void fireSocketClosedListener()
    {
        for (HTTPSocketListener l : _listeners)
        {
            l.onSocketClosed();
        }
    }
}

