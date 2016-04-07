package nl.hro.dta01.lesson.four.matrix;

import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.two.model.Tuple;

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
}
