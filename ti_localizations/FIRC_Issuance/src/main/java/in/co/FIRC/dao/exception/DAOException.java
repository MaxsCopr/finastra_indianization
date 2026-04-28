package in.co.FIRC.dao.exception;

public class DAOException
extends Exception
{
private static final long serialVersionUID = -6677728600384808574L;
public DAOException() {}
public DAOException(String msg)
{
  super(msg);
}
public DAOException(Throwable exception)
{
  super(exception);
}
public DAOException(String msg, Throwable exception)
{
  super(msg, exception);
}
}
