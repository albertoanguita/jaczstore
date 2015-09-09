package jacz.store.old;

import jacz.store.Database;
import jacz.store.common.Tag;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tags library
 */
public class TagLibrary extends ItemContainer<Tag> {

    TagLibrary(Database database, DBMediator dbMediator) {
        super("TagLibrary", database, dbMediator);
    }

    @Override
    protected List<String> defineFields() {
        List<String> tagFields = new ArrayList<>();
        tagFields.add("value");
        return tagFields;
    }

    @Override
    protected Tag loadItem(Map<String, String> record) throws ParseException {
//        return new Tag(database, record);
        return null;
    }

    @Override
    public Tag buildNewItem() throws ParseException, DBException, IOException {
//        return new Tag(database);
        return null;
    }
}
