package prj.httpApplication.app;

public abstract class Router
{
    public abstract void addRouting(String pattern, HTTPRequestHandler handler);

    public abstract HTTPRequestHandler getHandler(String resourceAddress);
}
