package nl.hro.dta01.lesson.four;


import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.User;
import nl.hro.dta01.lesson.two.model.Tuple;

import java.util.*;

import static nl.hro.dta01.lesson.four.Importer.loadDataMovieLens;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.calculateDeviation;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.predictRating;

public class Main {

    public static void main(String[] args) {

        Map<Integer, User> userRatings = loadDataMovieLens();

//        DeviationModel AB = calculateDeviation(1,2,new Tuple<>(5.0, 3.0),new Tuple<>(3.0, 4.0),new Tuple<>(4.0, 2.0));
//        DeviationModel AC = calculateDeviation(1,3,new Tuple<>(5.0, 2.0), new Tuple<>(4.0, 3.0));

//        System.out.println(
//            predictRating(
//                    new ArrayList<Tuple<Integer, Double>>() {
//                        {
//                            add(new Tuple<>(2, 2.0));
//                            add(new Tuple<>(3, 5.0));
//                        }
//                    },
//                    new ArrayList<DeviationModel>() {
//                        {
//                            add(AB);
//                            add(AC);
//                        }
//                    }
//            )
//        );

        long start = System.currentTimeMillis();

        Map<String,DeviationModel> deviationModels = new HashMap<>();
        for (int y = 1; y <= userRatings.size(); y++) {
            for (int x = 1; x <= userRatings.size(); x++) {
                if(y != x ) {
                    DeviationModel z = calculateDeviation(x, y, getRatings(userRatings, x, y));
                    if( z.getRaters() != 0 ) {
                        deviationModels.put( x + "-" + y, z );
                    }
                }
            }
        }

        long end = System.currentTimeMillis();

        System.out.println( String.format("calculating divs took %f seconds", (end-start) / 1000.0 ) );

        /**
         * todo : SlopeOne#predictRating(java.util.List, java.util.List)
         */

    }

    private static List<Tuple<Double, Double>> getRatings (Map<Integer, User> data, int itemIdA, int itemIdB) {
        Iterator<Integer> keyItr = data.keySet().iterator();
        List<Tuple<Double,Double>> ratings = new ArrayList<>();
        while (keyItr.hasNext()) {
            Integer key = keyItr.next();
            User user = data.get(key);
            if(user.hasRated(itemIdA) && user.hasRated(itemIdB)) {
                ratings.add(
                        new Tuple<>(
                                user.getRatingForItem(itemIdA),
                                user.getRatingForItem(itemIdB)
                        )
                );
            }
        }
        return ratings;
    }


}
