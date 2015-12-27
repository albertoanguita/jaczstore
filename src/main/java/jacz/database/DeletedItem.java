package jacz.database;

import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * A deleted item registered in the database
 */
public class DeletedItem extends DatabaseItem {

    DeletedItem(Model model, String dbPath) {
        super(model, dbPath);
    }

    public static List<DeletedItem> getDeletedItemsFromTimestamp(String dbPath, long fromTimestamp) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.DELETED_ITEM, DatabaseMediator.Field.TIMESTAMP.value + " >= ?", fromTimestamp));
    }

    static List<DeletedItem> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<DeletedItem> deletedItems = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    deletedItems.add(new DeletedItem(model, dbPath));
                }
            }
            return deletedItems;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }

    @Override
    protected DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.DELETED_ITEM;
    }

    public DatabaseMediator.ItemType getDeletedItemType() {
        return DatabaseMediator.ItemType.valueOf(getString(DatabaseMediator.Field.ITEM_TYPE));
    }

    public int getDeletedItemId() {
        return getInteger(DatabaseMediator.Field.ITEM_ID);
    }

    @Override
    public void merge(DatabaseItem anotherItem) {
        // ignore
    }

    @Override
    public void mergePostponed(DatabaseItem anotherItem) {
        // ignore
    }
}
