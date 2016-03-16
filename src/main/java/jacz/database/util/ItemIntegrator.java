package jacz.database.util;

import jacz.util.AI.inference.Mycin;
import jacz.util.numeric.NumericUtil;

import java.util.List;

/**
 * Code for integrating disparate database into a single one
 */
public class ItemIntegrator {

    /**
     * Threshold for considering two items equal
     */
    public static final float THRESHOLD = 0.9f;


    public static float genericNameSimilarity(String name1, String name2) {
        if (name1 != null && name2 != null) {
            if (name1.equals(name2)) {
                return 0.99f;
            } else if (name1.equalsIgnoreCase(name2)) {
                return 0.98f;
            } else {
                return -0.5f;
            }
        } else {
            return 0f;
        }
    }

    public static float personsNameSimilarity(String name1, String name2) {
        // todo include abbreviations (Will Smith, W. Smith).
        if (name1 != null && name2 != null) {
            if (name1.equals(name2)) {
                return 0.99f;
            } else if (name1.equalsIgnoreCase(name2)) {
                return 0.98f;
            } else {
                return -0.5f;
            }
        } else {
            return 0f;
        }
    }

    public static float creationsTitleSimilarity(String title1, String title2, String originalTitle1, String originalTitle2) {
        float titlesSimilarity = 0.0f;
        float title1WithOriginalTitle2 = -1.0f;
        float title2WithOriginalTitle1 = -1.0f;
        if (title1 != null && title2 != null) {
            titlesSimilarity = titlesSimilarity(title1, title2);
        }
        if (originalTitle1 != null && originalTitle2 != null) {
            titlesSimilarity = Mycin.combine(titlesSimilarity, titlesSimilarity(originalTitle1, originalTitle2));
        }
        if (title1 != null && originalTitle2 != null) {
            title1WithOriginalTitle2 = titlesSimilarity(title1, originalTitle2);
        }
        if (originalTitle1 != null && title2 != null) {
            title2WithOriginalTitle1 = titlesSimilarity(title2, originalTitle1);
        }
        return NumericUtil.max(titlesSimilarity, title1WithOriginalTitle2, title2WithOriginalTitle1);
    }

    public static float titlesSimilarity(String title1, String title2) {
        if (title1.equalsIgnoreCase(title2)) {
            return 0.9f;
        } else {
            return -0.1f;
        }
    }

//    public static float originalTitlesSimilarity(String title1, String title2) {
//        return 0f;
//    }

    public static float eventsYearSimilarity(Integer year1, Integer year2) {
        if (year1 != null && year2 != null) {
            if (year1.equals(year2)) {
                return 0.1f;
            } else if (Math.abs(year1 - year2) == 1) {
                return -0.2f;
            } else {
                return -0.90f;
            }
        } else {
            return 0f;
        }
    }

    public static float durationSimilarity(Integer minutes1, Integer minutes2) {
        if (minutes1 != null && minutes2 != null) {
            if (minutes1.equals(minutes2)) {
                return 0.2f;
            } else if (Math.abs(minutes1 - minutes2) < 3) {
                return 0.1f;
            } else if (Math.abs(minutes1 - minutes2) < 6) {
                return 0.05f;
            } else {
                return 0f;
            }
        } else {
            return 0f;
        }
    }

    public static <E> float addListSimilarity(float similarity, List<E> list1, List<E> list2, float confidence) {
        if (list1 == null || list2 == null) {
            return 0f;
        }
        int itemMatches = 0;
        for (E item : list1) {
            if (list2.contains(item)) {
                itemMatches++;
            }
        }
        float listSimilarity = evaluateListSimilarity(new ListSimilarity(list1.size(), list2.size(), itemMatches), confidence);
        return Mycin.combine(similarity, listSimilarity);
    }

//    public static float addListSimilarity(float similarity, Map<DatabaseMediator.ReferencedList, Float> listAndConfidencesMap, ListSimilarity... listSimilarities) {
//        for (ListSimilarity listSimilarity : listSimilarities) {
//            if (listAndConfidencesMap.containsKey(listSimilarity.referencedList)) {
//                similarity = Mycin.combine(similarity, evaluateListSimilarity(listSimilarity, listAndConfidencesMap.get(listSimilarity.referencedList)));
//            }
//        }
//        return similarity;
//    }

    private static float evaluateListSimilarity(ListSimilarity listSimilarity, float confidence) {
        int min = Math.min(listSimilarity.firstListSize, listSimilarity.secondListSize);
        if (min != 0) {
            return confidence * (listSimilarity.commonItems / Math.min(listSimilarity.firstListSize, listSimilarity.secondListSize));
        } else {
            return 0;
        }
    }


}
