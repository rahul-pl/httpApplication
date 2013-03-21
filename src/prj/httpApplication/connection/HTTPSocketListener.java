package prj.httpApplication.connection;

import prj.httpparser.httpparser.RawHTTPRequest;

public interface HTTPSocketListener
{
    public void onRequestArrived(RawHTTPRequest request);

    public void onRequestError();

    public void onSocketClosed();
}
