package nl.hro.dta01.lesson.four;


import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.two.model.Tuple;

import java.util.ArrayList;

import static nl.hro.dta01.lesson.four.matrix.SlopeOne.calculateDeviation;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.predictRating;

public class Main {

    public static void main(String[] args) {


        DeviationModel AB = calculateDeviation(
                1, 2,
                new Tuple<>(5.0, 3.0),
                new Tuple<>(3.0, 4.0),
                new Tuple<>(4.0, 2.0)
        );

        DeviationModel AC = calculateDeviation(
                1, 3,
                new Tuple<>(5.0, 2.0),
                new Tuple<>(4.0, 3.0)
        );

        System.out.println(
            predictRating(
                    new ArrayList<Tuple<Integer, Double>>() {
                        {
                            add(new Tuple<>(2, 2.0));
                            add(new Tuple<>(3, 5.0));
                        }
                    },
                    new ArrayList<DeviationModel>() {
                        {
                            add(AB);
                            add(AC);
                        }
                    }
            )
        );


    }
}
