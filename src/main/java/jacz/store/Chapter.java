package jacz.store;

import com.neovisionaries.i18n.CountryCode;
import jacz.store.database_old.DatabaseMediator;
import org.javalite.activejdbc.Model;

import java.util.List;

/**
 * Created by Alberto on 12/09/2015.
 */
public final class Chapter extends CreationItem {

    private String season;

    private int minutes;

    private List<VideoFile> videoFiles;

    public Chapter() {
        super();
    }

    @Override
    protected Model buildModel() {
        return new jacz.store.database.models.Chapter();
    }

    @Override
    Class<? extends Model> getPeopleAssociationModel() {
        return jacz.store.database.models.ChaptersPeople.class;
    }

    @Override
    String getAssociationIdField() {
        return "chapter_id";
    }

}
