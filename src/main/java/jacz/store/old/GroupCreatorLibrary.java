package jacz.store.old;

import jacz.store.Database;
import jacz.store.common.GroupCreator;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Group creators library
 */
public class GroupCreatorLibrary extends ItemContainer<GroupCreator> {

    GroupCreatorLibrary(Database database, DBMediator dbMediator) {
        super("AudioAlbumLibrary", database, dbMediator);
    }

    @Override
    protected List<String> defineFields() {
        List<String> groupCreatorFields = new ArrayList<>();
        groupCreatorFields.add("identifier");
        groupCreatorFields.add("creationDate");
        groupCreatorFields.add("modificationDate");
        groupCreatorFields.add("name");
        groupCreatorFields.add("persons");
        return groupCreatorFields;
    }

    @Override
    protected GroupCreator loadItem(Map<String, String> record) throws ParseException {
//        return new GroupCreator(database, record);
        return null;
    }

    @Override
    public GroupCreator buildNewItem() throws ParseException, DBException, IOException {
//        return new GroupCreator(database);
        return null;
    }
}
