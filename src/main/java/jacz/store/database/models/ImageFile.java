package jacz.store.database.models;

import jacz.store.database.DatabaseMediator;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * ImageFile model (table image_files)
 */
@Table("image_files")
public class ImageFile extends Model {

    @Override
    public void beforeDelete() {
        if (DatabaseMediator.mustAutoComplete()) {
            Movie.deleteImageLink(this);
            TVSeries.deleteImageLink(this);
            DeletedItem.addDeletedItem(this, getTableName());
        }
    }

    static void deleteRecord(Model fromModel) {
        ImageFile imageFile = fromModel.parent(ImageFile.class);
        if (imageFile != null) {
            imageFile.delete();
        }
    }
}
