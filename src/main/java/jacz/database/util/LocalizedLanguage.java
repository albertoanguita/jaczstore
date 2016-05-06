package jacz.database.util;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A language with an optional localization (country)
 */
public class LocalizedLanguage implements Serializable {

    public static final String SEPARATOR = "/";

    public static final String NULL = "NULL";

    public final LanguageCode language;

    public final CountryCode country;

    public LocalizedLanguage(LanguageCode language) {
        this(language, null);
    }

    public LocalizedLanguage(LanguageCode language, CountryCode country) {
        if (language == null) {
            throw new NullPointerException("Language cannot be null");
        }
        this.language = language;
        this.country = country;
    }

    public static LocalizedLanguage deserialize(String serialization) {
        if (serialization != null) {
            StringTokenizer strTok = new StringTokenizer(serialization, SEPARATOR);
            if (strTok.hasMoreTokens()) {
                String languageStr = strTok.nextToken();
                LanguageCode language = LanguageCode.valueOf(languageStr);
                if (strTok.hasMoreTokens()) {
                    String countryStr = deserializeCountry(strTok.nextToken());
                    CountryCode country = countryStr != null ? CountryCode.valueOf(countryStr) : null;
                    return new LocalizedLanguage(language, country);
                }
            }
            throw new NullPointerException("Invalid serialization: " + serialization);
        } else {
            return null;
        }
    }

    private static String deserializeCountry(String countrySerialization) {
        if (countrySerialization.equals(NULL)) {
            return null;
        } else {
            return countrySerialization;
        }
    }

    public static List<LocalizedLanguage> deserialize(List<String> localizedLanguagesStr) {
        List<LocalizedLanguage> list = new ArrayList<>();
        for (String str : localizedLanguagesStr) {
            list.add(LocalizedLanguage.deserialize(str));
        }
        return list;
    }

    public String serialize() {
        String countrySerialization = country != null ? country.getAlpha2() : NULL;
        return language.name() + SEPARATOR + countrySerialization;
    }

    public static String serialize(LocalizedLanguage localizedLanguage) {
        return localizedLanguage != null ? localizedLanguage.serialize() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalizedLanguage that = (LocalizedLanguage) o;

        if (language != that.language) return false;
        return country == that.country;

    }

    @Override
    public int hashCode() {
        int result = language.hashCode();
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return serialize();
    }
}
