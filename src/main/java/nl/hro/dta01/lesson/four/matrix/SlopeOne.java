package nl.hro.dta01.lesson.four.matrix;

import com.j256.ormlite.stmt.query.In;
import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.two.model.Tuple;

import java.util.List;

public class SlopeOne {

    @SafeVarargs
    public static DeviationModel calculateDeviation(int a,
                                                    int b,
                                                    Tuple<Double, Double>... ratings)
    {
        double numerator = 0;
        int denominator  = ratings.length;
        for (Tuple<Double, Double> rating : ratings) {
            numerator += ( rating.getA() - rating.getB() );
        }
        return new DeviationModel(
                a,
                b,
                denominator,
                numerator/denominator
        );
    }

    @SafeVarargs
    public static DeviationModel updateDeviationModel(DeviationModel model,
                                                      Tuple<Double, Double>... ratings)
    {
        double numerator = model.getDiv() * model.getRaters();
        int denominator  = model.getRaters() + ratings.length;
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

    public static double predictRating(List<Tuple<Integer, Double>> uRatings, List<DeviationModel> deviationModels) {

        double numerator = 0;
        int denominator  = 0;

        for (int i = 0; i < uRatings.size(); i++) {
            Tuple<Integer, Double> rating = uRatings.get(i);
            DeviationModel deviationModel = deviationModels.get(i);

            numerator   += (rating.getA() + deviationModel.getDiv()) * deviationModel.getRaters();
            denominator += deviationModel.getRaters();
        }

        return numerator/denominator;
    }
}
