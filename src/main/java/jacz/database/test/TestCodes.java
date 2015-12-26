package jacz.database.test;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageCode;

/**
 * Created by Alberto on 16/11/2015.
 */
public class TestCodes {

    public static void main(String[] args) {

        CountryCode countryCode = CountryCode.valueOf("ES");

        System.out.println(countryCode.getName());
        System.out.println(countryCode.getAlpha2());
        System.out.println(countryCode.getAlpha3());
        System.out.println(countryCode.getCurrency());
        System.out.println(countryCode.getNumeric());


        LanguageCode languageCode = LanguageCode.es;
        System.out.println(languageCode.name());
        System.out.println(languageCode.getAlpha3().toString());
    }
}
