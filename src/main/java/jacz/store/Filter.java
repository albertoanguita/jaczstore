package jacz.store;

/**
 * A filter for querying a database
 */
public class Filter {

    public enum Operator {
        EQUALS,
        NOT_EQUALS,
        LESS,
        LESS_THAN,
        GREATER,
        GREATER_THAN,
        CONTAINS
    }

    public final String field;

    public final Operator operator;

    public final String value;

    public Filter(String field, Operator operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }
}
