package prj.httpApplication.connection;

import prj.cyclo.Agent;
import prj.httpparser.characterparse.CharParser;
import prj.httpparser.httpparser.HTTPParser;
import prj.httpparser.wordparser.WordParser;

import java.net.Socket;

public class HTTPSocketFactory
{
    private Agent _agent;

    public HTTPSocketFactory(Agent agent)
    {
        _agent = agent;
    }

    public HTTPSocket newSocket(Socket socket)
    {
        return new HTTPSocket(_agent, socket, new HTTPParser(new WordParser(new CharParser())));
    }
}

