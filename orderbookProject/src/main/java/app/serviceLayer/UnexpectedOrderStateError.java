package app.serviceLayer;

public class UnexpectedOrderStateError extends Exception{
  public UnexpectedOrderStateError(String msg) {
    super(msg);
  }

  public UnexpectedOrderStateError(String msg, Throwable cause){
    super(msg, cause);
  }
}
