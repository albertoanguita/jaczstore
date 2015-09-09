package jacz.store.common;

import jacz.util.io.SixBitSerializer;

/**
 * Factory for identifier objects
 * <p/>
 * Identifiers are local to a database but global to containers; two databases might share identifiers,
 * but two containers may not share an id.
 * <p/>
 * We use 60-bit identifiers, which is enough for the library items that a user will ever create. We store them with the 6-bit character codification
 * which generates Strings of length 10. This gives 1 trillion possible ids. Since ids are local, this is enough
 */
public class IdentifierFactory {

    private static final int LENGTH = 10;

    private final StringBuilder nextId;

    public IdentifierFactory() {
        nextId = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            nextId.append(SixBitSerializer.FIRST_CHAR);
        }
    }

    public IdentifierFactory(String nextId) {
        this.nextId = new StringBuilder(nextId);
    }

    public String getOneIdentifier() {
        String identifier = nextId.toString();
        increaseId();
        return identifier;
    }

    private void increaseId() {
        boolean finished = false;
        int pos = LENGTH - 1;
        while (!finished) {
            nextId.setCharAt(pos, SixBitSerializer.getNextChar(nextId.charAt(pos)));
            if (nextId.charAt(pos) == SixBitSerializer.FIRST_CHAR && pos >= 0) {
                // finished this position, move to previous one
                pos--;
                if (pos < 0) {
                    pos = LENGTH - 1;
                }
            } else {
                finished = true;
            }
        }
    }

    public String save() {
        return nextId.toString();
    }

    public static IdentifierFactory load(String save) {
        return new IdentifierFactory(save);
    }

    @Override
    public String toString() {
        return "nextId=" + nextId;
    }
}
