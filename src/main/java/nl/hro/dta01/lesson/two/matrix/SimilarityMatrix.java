package nl.hro.dta01.lesson.two.matrix;

import nl.hro.dta01.lesson.two.model.UserPreference;

public class SimilarityMatrix {

    private static SimilarityMatrix instance;

    public static SimilarityMatrix getInstance() {
        if (instance == null) {
            instance = new SimilarityMatrix();
        }
        return instance;
    }

    private SimilarityMatrix() {
        // Singleton class
    }

    /**
     * TODO get matrix based on value set
     */
    public double calculateSimilarity(UserPreference a, UserPreference b) {
        SimilarityStrategy similarityStrategy = new PearsonCoefficient();

        return similarityStrategy.calculateSimilarity(a,b);
    }
}
