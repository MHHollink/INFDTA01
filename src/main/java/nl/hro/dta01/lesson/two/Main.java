package nl.hro.dta01.lesson.two;

import nl.hro.dta01.lesson.two.model.UserPreference;
import nl.hro.dta01.lesson.two.matrix.SimilarityMatrix;

import java.util.HashMap;
import java.util.Map;

public class Main {

    static Map<String, UserPreference> userPrefrences;

    static double[] x = {4.75,4.5,5,4.25,4};
    static double[] y = {4,3,5,2,1};

    public static void main(String[] args) {
        userPrefrences = new HashMap<>();

        userPrefrences.put("Marcel", new UserPreference());
        userPrefrences.put("Evert-Jan", new UserPreference());

        for (int i = 0; i < x.length; i++) {
            userPrefrences.get("Marcel").put(String.valueOf(i),x[i]);
            userPrefrences.get("Evert-Jan").put(String.valueOf(i),y[i]);
        }

        double sim = SimilarityMatrix.getInstance()
                .calculateSimilarity(
                        userPrefrences.get("Marcel"),
                        userPrefrences.get("Evert-Jan")
                );
        System.out.println(sim);
    }
}
