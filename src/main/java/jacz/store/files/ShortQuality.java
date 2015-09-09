package jacz.store.files;

import java.text.ParseException;

/**
 * A 5-value quality classification
 */
public enum ShortQuality {

    _1,
    _2,
    _3,
    _4,
    _5;

    public static ShortQuality parse(String s) throws ParseException {
        for (ShortQuality shortQuality : ShortQuality.values()) {
            if (shortQuality.toString().equals(s)) {
                return shortQuality;
            }
        }
        throw new ParseException("Invalid string: " + s, 0);
    }
}
