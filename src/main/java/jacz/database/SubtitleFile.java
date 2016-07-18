package jacz.database;

import com.neovisionaries.i18n.LanguageCode;
import jacz.database.util.LocalizedLanguage;
import org.javalite.activejdbc.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class SubtitleFile extends File {

//    private LanguageCode language;

    SubtitleFile(String dbPath) {
        super(dbPath);
    }

    public SubtitleFile(String dbPath, String hash) {
        super(dbPath, hash);
    }

    public SubtitleFile(String dbPath, Integer id) {
        super(dbPath, id);
    }

    SubtitleFile(Model model, String dbPath) {
        super(model, dbPath);
    }

    @Override
    public DatabaseMediator.ItemType getItemType() {
        return DatabaseMediator.ItemType.SUBTITLE_FILE;
    }

    @Override
    public boolean isOrphan() {
        return getElementsContainingMe(DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.Field.SUBTITLE_FILE_LIST).isEmpty();
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

    public LocalizedLanguage getLocalizedLanguage() {
        return LocalizedLanguage.deserialize(getString(DatabaseMediator.Field.LOCALIZED_LANGUAGE));
    }

    public void setLocalizedLanguage(LocalizedLanguage localizedLanguage) {
        set(DatabaseMediator.Field.LOCALIZED_LANGUAGE, LocalizedLanguage.serialize(localizedLanguage), true);
    }

    public void setLocalizedLanguagePostponed(LocalizedLanguage localizedLanguage) {
        set(DatabaseMediator.Field.LOCALIZED_LANGUAGE, LocalizedLanguage.serialize(localizedLanguage), false);
    }

    public List<VideoFile> getVideoFiles() {
        try {
            connect();
            List<jacz.database.models.VideoFile> modelVideoFiles = getElementsContainingMe(DatabaseMediator.ItemType.VIDEO_FILE, DatabaseMediator.Field.SUBTITLE_FILE_LIST);
            if (modelVideoFiles != null) {
                return VideoFile.buildList(dbPath, modelVideoFiles);
            } else {
                return new ArrayList<>();
            }
        } finally {
            disconnect();
        }
    }

    @Override
    public void mergeBasicPostponed(DatabaseItem anotherItem) {
        super.mergeBasicPostponed(anotherItem);
        SubtitleFile subtitleFile = (SubtitleFile) anotherItem;
        super.mergeBasicPostponed(anotherItem);
        if (getLocalizedLanguage() == null && subtitleFile.getLocalizedLanguage() != null) {
            setLocalizedLanguagePostponed(subtitleFile.getLocalizedLanguage());
        }
    }

    @Override
    public void delete() {
        // check any tv series pointing to me, delete their reference to me
        for (VideoFile videoFile : getVideoFiles()) {
            videoFile.removeSubtitleFile(this);
        }
        super.delete();
    }

    @Override
    public String toString() {
        return super.toString() +
                ", localized language=" + getLocalizedLanguage();
    }
}
