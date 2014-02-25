package player;

/*
 * Implements an Exception that signals an attempt to insert a chip into an invalid cell such as the four corners.
 */
public class InvalidCellException extends Exception {
  protected InvalidCellException() {
    super();
  }

  protected InvalidCellException(String s) {
    super(s);
  }
}