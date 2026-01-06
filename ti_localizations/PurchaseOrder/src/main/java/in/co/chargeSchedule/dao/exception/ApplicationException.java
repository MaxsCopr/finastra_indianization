package in.co.chargeSchedule.dao.exception;

import java.io.PrintStream;

public class ApplicationException
  extends Exception
{
  private static final long serialVersionUID = 1L;
  Throwable exceptionClass = null;
  public ApplicationException(String msg)
  {
    super(msg);
  }
  public ApplicationException(String msg, Throwable exception)
  {
    super(msg, exception);
    this.exceptionClass = exception;
  }
  public void printStackTrace()
  {
    if (this.exceptionClass != null)
    {
      System.err.println("An exception has caused by " + 
        this.exceptionClass.toString());
      this.exceptionClass.printStackTrace();
    }
  }
}
