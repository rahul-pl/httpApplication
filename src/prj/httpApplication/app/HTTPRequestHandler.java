package prj.httpApplication.app;

import prj.httpparser.httpparser.RawHTTPRequest;
import prj.httpApplication.RawHTTPResponse;

public abstract class HTTPRequestHandler
{
    private final RawHTTPResponse pageNotFoundResponse = new RawHTTPResponse("HTTP/1.0", 404, "Not Found")
    {
        {
            setBody("Page Not Found");
        }
    };

    public RawHTTPResponse get(RawHTTPRequest request)
    {
        return pageNotFoundResponse;
    }

    public RawHTTPResponse post(RawHTTPRequest request)
    {
        return pageNotFoundResponse;
    }
}
