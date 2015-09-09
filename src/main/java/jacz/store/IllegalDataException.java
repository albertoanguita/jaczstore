package jacz.store;

/**
 * Exception for errors managing the database
 */
public class IllegalDataException extends Exception {

    public IllegalDataException(String message) {
        super(message);
    }
}
