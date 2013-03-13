package prj.httpApplication.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prj.cyclo.Agent;
import prj.cyclo.TCPReactor;
import prj.httpApplication.app.WebApp;
import prj.httpApplication.connection.HTTPSocket;
import prj.httpApplication.connection.HTTPSocketFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HTTPAgent extends Agent
{
    private int _port = 8080;
    private int _socketTimeOutInSeconds = 3;
    private final HTTPSocketFactory _socketFactory;
    private Map<Socket, ScheduledFuture> _cancellableFutures;
    private Map<Socket, HTTPSocket> _sockets;
    private Logger _logger;
    private WebApp _application;

    public HTTPAgent(TCPReactor reactor, WebApp application, int port, int socketTimeOutInSeconds)
    {
        super(reactor);
        _port = port;
        _socketTimeOutInSeconds = socketTimeOutInSeconds;
        _socketFactory = new HTTPSocketFactory(this);
        _sockets = new HashMap<>();
        _cancellableFutures = new HashMap<>();
        _application = application;
        _logger = LoggerFactory.getLogger(HTTPAgent.class.getSimpleName());
    }

    public HTTPAgent(TCPReactor reactor, WebApp application, int port, int socketTimeOutInSeconds, ScheduledThreadPoolExecutor executor)
    {
        super(reactor, executor);
        _port = port;
        _socketTimeOutInSeconds = socketTimeOutInSeconds;
        _socketFactory = new HTTPSocketFactory(this);
        _sockets = new HashMap<>();
        _cancellableFutures = new HashMap<>();
        _application = application;
        _logger = LoggerFactory.getLogger(HTTPAgent.class.getSimpleName());
    }

    @Override
    public void connectionMade(final Socket socket)
    {
        if (_socketTimeOutInSeconds > 0)
        {
            ScheduledFuture future = _agency.schedule(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        socket.close();
                    }
                    catch (IOException e)
                    {
                        _logger.warn("exception while closing socket", e);
                    }
                    _cancellableFutures.remove(socket);
                }
            }, _socketTimeOutInSeconds, TimeUnit.SECONDS);
            _cancellableFutures.put(socket, future);
        }
    }

    @Override
    public boolean isServer()
    {
        return true;
    }

    @Override
    public InetSocketAddress getSocketAddress() throws UnknownHostException
    {
        return new InetSocketAddress("0.0.0.0", _port);
    }

    @Override
    public void receive(Socket socket, byte[] incomingData)
    {
        getSocket(socket).parse(new String(incomingData));
    }

    private HTTPSocket getSocket(Socket socket)
    {
        HTTPSocket httpSocket;
        if ((httpSocket = _sockets.get(socket)) == null)
        {
            if (_cancellableFutures.containsKey(socket))
            {
                _cancellableFutures.get(socket).cancel(true);
                _cancellableFutures.remove(socket);
            }
            _sockets.put(socket, (httpSocket = _socketFactory.newSocket(socket)));
            _application.clientConnected(httpSocket);
        }
        return httpSocket;
    }

    @Override
    public void registrationFailed(IOException e)
    {
        _logger.debug("registration failed for httpAgent on port " + _port);
    }

    @Override
    public void close(Socket socket)
    {
        super.close(socket);
        _sockets.remove(socket);
    }

    @Override
    public void onClose(Socket socket)
    {
        HTTPSocket httpSocket = _sockets.remove(socket);
        if (httpSocket != null)
        {
            httpSocket.close();
        }
    }
}

