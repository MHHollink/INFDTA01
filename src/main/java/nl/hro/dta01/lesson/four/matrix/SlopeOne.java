package nl.hro.dta01.lesson.four.matrix;

import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.Tuple;

import java.util.List;

public class SlopeOne {

    /**
     * @param a         = The item id for product A
     * @param b         = The item id for product B
     * @param ratings   = A list of all the ratings for <A, B>
     * @return The {@link DeviationModel} for A B with created with {@link DeviationModel#DeviationModel(int, int, int, double)}
     */
    public static DeviationModel calculateDeviation(int a,
                                                    int b,
                                                    List<Tuple<Double, Double>> ratings)
    {
        double numerator = 0;
        int denominator  = ratings.size();
        for (Tuple<Double, Double> rating : ratings) {
            numerator += ( rating.getA() - rating.getB() );
        }
        return denominator == 0 ? new DeviationModel(a,b,denominator,0) : new DeviationModel(a, b, denominator, numerator/denominator);
    }

    /**
     * @param model     = the current deviation model for products A and B
     * @param ratings   = the list of added ratings
     * @return          = an updated {@link DeviationModel}
     */
    public static DeviationModel updateDeviationModel(DeviationModel model,
                                                      List<Tuple<Double, Double>> ratings)
    {
        double numerator = model.getDiv() * model.getRaters();
        int denominator  = model.getRaters() + ratings.size();
        for (Tuple<Double, Double> rating : ratings) {
            numerator += ( rating.getA() - rating.getB() );
        }
        return new DeviationModel(
                model.getItemIdA(),
                model.getItemIdB(),
                denominator,
                numerator/denominator
        );
    }

    /**
     * @param uRatings              = Ratings of the target user
     * @param deviationModels       = {@link DeviationModel}'s of all the i items the target item
     * @return a {@link Double} predicting the rating of the targeted item
     */
    public static double predictRating(List<Tuple<Integer, Double>> uRatings, List<DeviationModel> deviationModels) {

        double numerator = 0;
        int denominator  = 0;

        for (int i = 0; i < uRatings.size(); i++) {
            Tuple<Integer, Double> rating = uRatings.get(i);
            DeviationModel deviationModel = deviationModels.get(i);

            numerator   += (rating.getB() + deviationModel.getDiv()) * deviationModel.getRaters();
            denominator += deviationModel.getRaters();
        }

        return numerator/denominator;
    }
}
