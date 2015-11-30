package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;

/**
 * Deleted items model (table deleted_items)
 */
public class DeletedItem extends Model {

    static void addDeletedItem(Model model, String tableName) {
        new DeletedItem().set("item_table", tableName).set("item_id", model.getInteger("id")).set("timestamp", DatabaseMediator.getNewTimestamp()).saveIt();
    }
}
