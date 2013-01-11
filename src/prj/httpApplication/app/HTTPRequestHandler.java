package prj.httpApplication.app;

import prj.httpparser.httpparser.RawHTTPRequest;
import prj.httpApplication.RawHTTPResponse;

public abstract class HTTPRequestHandler
{
    public RawHTTPResponse get(RawHTTPRequest request)
    {
        return new RawHTTPResponse("HTTP/1.0", 405, "This method is not allowed");
    }

    public RawHTTPResponse post(RawHTTPRequest request)
    {
        return new RawHTTPResponse("HTTP/1.0", 405, "This method is not allowed");
    }
}
