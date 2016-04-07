package nl.hro.dta01.lesson.four;


import nl.hro.dta01.lesson.four.matrix.SlopeOne;
import nl.hro.dta01.lesson.two.model.Tuple;

import static nl.hro.dta01.lesson.four.matrix.SlopeOne.*;

public class Main {

    public static void main(String[] args) {


        calculateDeviation(
                2, 3,
                new Tuple<>(4.0, 3.0),
                new Tuple<>(5.0, 2.0)
        ).getDiv();


    }
}
