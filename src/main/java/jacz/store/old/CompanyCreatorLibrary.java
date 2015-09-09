package jacz.store.old;

import jacz.store.Database;
import jacz.store.common.CompanyCreator;
import jacz.store.db_mediator.DBException;
import jacz.store.db_mediator.DBMediator;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Company creators library
 */
public class CompanyCreatorLibrary extends ItemContainer<CompanyCreator> {

    CompanyCreatorLibrary(Database database, DBMediator dbMediator) {
        super("AudioAlbumLibrary", database, dbMediator);
    }

    @Override
    protected List<String> defineFields() {
        List<String> companyCreatorFields = new ArrayList<>();
        companyCreatorFields.add("identifier");
        companyCreatorFields.add("creationDate");
        companyCreatorFields.add("modificationDate");
        companyCreatorFields.add("name");
        return companyCreatorFields;
    }

    @Override
    protected CompanyCreator loadItem(Map<String, String> record) throws ParseException {
//        return new CompanyCreator(database, record);
        return null;
    }

    @Override
    public CompanyCreator buildNewItem() throws ParseException, DBException, IOException {
//        return new CompanyCreator(database);
        return null;
    }
}
