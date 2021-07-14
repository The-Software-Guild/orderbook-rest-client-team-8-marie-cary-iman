package app.serviceLayer;

public class UnexpectedClientStateError extends Exception {
  public UnexpectedClientStateError(String msg) {
    super(msg);
  }

  public UnexpectedClientStateError(String msg, Throwable cause) { super(msg, cause); }
}
