package prj.httpApplication;

import java.util.HashMap;
import java.util.Map;

public class RawHTTPResponse
{
    private String httpVersion;
    private int responseCode;
    private String reasonPhrase;
    private Map<String, String> headers;
    private String body;
    private static String SP = " ";
    private static String CRLF = "\r\n";
    private static final Object COLON = ":";

    public RawHTTPResponse(String httpVersion, int responseCode, String reasonPhrase)
    {
        this.httpVersion = httpVersion;
        this.responseCode = responseCode;
        this.reasonPhrase = reasonPhrase;
        this.headers = new HashMap<>();
    }

    public String getHttpVersion()
    {
        return httpVersion;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public String getReasonPhrase()
    {
        return reasonPhrase;
    }

    public void setHttpVersion(String httpVersion)
    {
        this.httpVersion = httpVersion;
    }

    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }

    public void setReasonPhrase(String reasonPhrase)
    {
        this.reasonPhrase = reasonPhrase;
    }

    public void addHeader(String field, String value)
    {
        headers.put(field, value);
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(httpVersion).append(SP).append(responseCode).append(SP).append(reasonPhrase).append(CRLF);
        for (String headerField : headers.keySet())
        {
            sb.append(headerField).append(COLON).append(SP).append(headers.get(headerField)).append(CRLF);
        }
        sb.append(CRLF);
        if (body != null)
        {
            sb.append(body);
        }
        return sb.toString();
    }
}
