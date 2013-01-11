package prj.httpApplication.app;

import prj.httpparser.httpparser.RawHTTPRequest;
import prj.httpApplication.connection.HTTPSocket;
import prj.httpApplication.connection.HTTPSocketListener;

public class WebApp
{
    public Router _router;

    public WebApp(Router router)
    {
        _router = router;
    }

    public void registerHandler(String pattern, HTTPRequestHandler handler)
    {
        _router.addRouting(pattern, handler);
    }

    public void clientConnected(final HTTPSocket httpSocket)
    {
        httpSocket.addListener(new HTTPSocketListener()
        {
            @Override
            public void onRequestArrived(RawHTTPRequest request)
            {
                HTTPRequestHandler handler = _router.getHandler(request.getResourceAddress());
                switch (request.getRequestType())
                {
                    case GET:
                        httpSocket.send(handler.get(request).toString());
                        httpSocket.close();
                        break;
                    case POST:
                        httpSocket.send(handler.post(request).toString());
                        httpSocket.close();
                        break;
                }
            }
        });
    }
}
