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

    public double calculateSimilarity(boolean dataIsComplete, UserPreference a, UserPreference b) {
        SimilarityStrategy similarityStrategy;

        if(!dataIsComplete) {
            similarityStrategy = new CosineSimilarity();
        } else {
            similarityStrategy = new PearsonCoefficient();
        }

        return similarityStrategy.calculateSimilarity(a,b);
    }

    public double calculateSimilarityUsingGivenAlgorithm(boolean dataIsComplete, UserPreference a, UserPreference b, Class<? extends SimilarityStrategy> strategy) {
        SimilarityStrategy similarityStrategy = null;

        try {
            similarityStrategy = strategy.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if(similarityStrategy == null)
            similarityStrategy = new CosineSimilarity();

        return similarityStrategy.calculateSimilarity(a,b);
    }
}
