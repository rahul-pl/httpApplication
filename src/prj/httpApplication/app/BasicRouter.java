package prj.httpApplication.app;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class BasicRouter extends Router
{
    public Map<String, HTTPRequestHandler> _routeMap;
    public HTTPRequestHandler _invalidRequestHandler;

    public BasicRouter()
    {
        _routeMap = new HashMap<>();
        _invalidRequestHandler = new HTTPRequestHandler(){};
    }

    @Override
    public void addRouting(String regex, HTTPRequestHandler handler)
    {
        _routeMap.put(regex, handler);
    }

    @Override
    public HTTPRequestHandler getHandler(String resourceAddress)
    {
        for (String regex : _routeMap.keySet())
        {
            if (Pattern.matches(regex, resourceAddress))
            {
                return _routeMap.get(regex);
            }
        }
        return _invalidRequestHandler;
    }
}
