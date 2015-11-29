package jacz.store.database.models;

import org.javalite.activejdbc.Model;

/**
 * Deleted items model (table deleted_items)
 */
public class DeletedItem extends Model {

    static void addDeletedItem(Model model, String tableName) {
        new DeletedItem().set("item_table", tableName).set("item_id", model.getInteger("id")).set("timestamp", model.getInteger("timestamp")).saveIt();
    }
}
