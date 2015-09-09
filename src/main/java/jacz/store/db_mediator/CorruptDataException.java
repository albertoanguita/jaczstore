package jacz.store.db_mediator;

/**
 * Created with IntelliJ IDEA.
 * User: Alberto
 * Date: 18/01/14
 * Time: 0:44
 * To change this template use OldFile | Settings | OldFile Templates.
 */
public class CorruptDataException extends Exception {

    public CorruptDataException(String message) {
        super(message);
    }
}
