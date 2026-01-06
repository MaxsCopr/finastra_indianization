package in.co.boe.businessdelegate.exception;

public class BusinessException
extends Exception
{
private static final long serialVersionUID = 1L;
public BusinessException() {}
public BusinessException(String msg)
{
  super(msg);
}
public BusinessException(String msg, Throwable exception)
{
  super(msg, exception);
}
}
