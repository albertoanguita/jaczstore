package jacz.store.database.models;

import org.javalite.activejdbc.Model;

/**
 * Movie model (table movies)
 */
public class Movie extends Model {

    @Override
    public void beforeDelete() {
        System.out.println("Movie deleted!!!");
    }

    @Override
    public void afterDelete() {
        System.out.println("Movie deleted after!!!");
    }
}
