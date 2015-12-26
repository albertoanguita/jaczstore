package jacz.database;

import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class SubtitleFile extends FileWithLanguages {

//    private LanguageCode language;

    public SubtitleFile(String dbPath) {
        super(dbPath);
    }

    SubtitleFile(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    protected DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.SUBTITLE_FILE;
    }

    public static List<SubtitleFile> getSubtitleFiles(String dbPath) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.SUBTITLE_FILE));
    }

    public static SubtitleFile getSubtitleFileById(String dbPath, int id) {
        Model model = getModelById(dbPath, DatabaseMediator.ItemType.SUBTITLE_FILE, id);
        return model != null ? new SubtitleFile(model, dbPath) : null;
    }

    public static List<SubtitleFile> getSubtitleFilesFromTimestamp(String dbPath, long fromTimestamp) {
        return buildList(dbPath, getModels(dbPath, DatabaseMediator.ItemType.SUBTITLE_FILE, DatabaseMediator.Field.TIMESTAMP.value + " >= ?", fromTimestamp));
    }

    static List<SubtitleFile> buildList(String dbPath, List<? extends Model> models) {
        DatabaseMediator.connect(dbPath);
        try {
            List<SubtitleFile> subtitleFiles = new ArrayList<>();
            for (Model model : models) {
                if (model != null) {
                    subtitleFiles.add(new SubtitleFile(model, dbPath));
                }
            }
            return subtitleFiles;
        } finally {
            DatabaseMediator.disconnect(dbPath);
        }
    }
}
