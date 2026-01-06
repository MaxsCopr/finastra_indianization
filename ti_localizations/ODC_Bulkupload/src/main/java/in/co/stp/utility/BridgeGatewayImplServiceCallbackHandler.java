package in.co.stp.utility;

public abstract class BridgeGatewayImplServiceCallbackHandler
{
  protected Object clientData;
  public BridgeGatewayImplServiceCallbackHandler(Object clientData)
  {
    this.clientData = clientData;
  }
  public BridgeGatewayImplServiceCallbackHandler()
  {
    this.clientData = null;
  }
  public Object getClientData()
  {
    return this.clientData;
  }
  public void receiveErrorprocess(Exception e) {}
}
