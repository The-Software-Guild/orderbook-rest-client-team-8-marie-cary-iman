package app.serviceLayer;

public class OrderDoesNotExistError extends Exception{
  public OrderDoesNotExistError (String msg) {
    super(msg);
  }

  public OrderDoesNotExistError (String msg, Throwable cause){
    super(msg, cause);
  }
}
