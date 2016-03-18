package nl.hro.dta01.lesson.two.matrix;

import nl.hro.dta01.lesson.two.model.UserPreference;

public class CosineSimilarity implements SimilarityStrategy {

    protected CosineSimilarity(){}

    @Override
    public double calculateSimilarity(UserPreference target, UserPreference other) {
        double a = 0, b = 0, c= 0;
        for (int i = 0; i < target.getRatings().size(); i++) {
            int product = (int) target.getRatings().keySet().toArray()[i];
            double x = target.getRatings().get(product);
            double y = 0;

            if(other.getRatings().get(product) != null) {
                y = other.getRatings().get(product);
            }

            a += x * y;
            b += Math.pow(x, 2);
            c += Math.pow(y, 2);
        }

        return (a) / ( Math.sqrt(b)* Math.sqrt(c));
    }
}
