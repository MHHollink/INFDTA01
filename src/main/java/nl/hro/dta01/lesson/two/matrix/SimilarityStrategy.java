package nl.hro.dta01.lesson.two.matrix;

import nl.hro.dta01.lesson.two.model.UserPreference;

public interface SimilarityStrategy {

    double calculateSimilarity(UserPreference a, UserPreference b);
}
