package jacz.database.models;

import jacz.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

/**
 * Deleted items model (table deleted_items)
 */
public class DeletedItem extends Model {

    static void addDeletedItem(Model model, String tableName) {
        new DeletedItem()
                .set(DatabaseMediator.Field.ITEM_TYPE.value, DatabaseMediator.getItemType(tableName).name())
                .set(DatabaseMediator.Field.ITEM_ID.value, model.getInteger(DatabaseMediator.Field.ID.value))
                .set(DatabaseMediator.Field.TIMESTAMP.value, DatabaseMediator.getNewTimestamp())
                .saveIt();
    }
}
