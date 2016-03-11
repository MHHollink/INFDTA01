package nl.hro.dta01.lesson.two.matrix;

import nl.hro.dta01.lesson.two.model.UserPreference;

public class PearsonCoefficient implements SimilarityStrategy {

    protected PearsonCoefficient(){}

    @Override
    public double calculateSimilarity(UserPreference target, UserPreference other) {
        double a = 0, b = 0, c = 0, d = 0, e = 0;
        int n = target.getRatings().size();
        for (int i = 0; i < n; i++) {
            String product = (String) target.getRatings().keySet().toArray()[i];
            double x = target.getRatings().get(product);
            double y = other.getRatings().get(product);

            a += x * y;
            b += x;
            c += y;
            d += Math.pow(x,2);
            e += Math.pow(y,2);
        }
        return ( a - ( (b*c) / n ) ) / ( Math.sqrt ( d - (Math.pow(b,2) / n) ) * ( Math.sqrt( e - (Math.pow(c,2) / n) ) ) );
    }
}
