package nl.hro.dta01.lesson.four;


import com.tantaman.commons.concurrent.Parallel;
import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.User;
import nl.hro.dta01.lesson.two.model.Tuple;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static nl.hro.dta01.lesson.four.Importer.loadDataMovieLens;
import static nl.hro.dta01.lesson.four.Importer.loadDataUserItem;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.calculateDeviation;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.predictRating;

public class Main {

    public static void main(String[] args) {

        long start; // used for timers
        long end;   // used for timers

        start = System.currentTimeMillis(); // START LOADING
        Map<Integer, User> userRatings = loadDataMovieLens();
        end = System.currentTimeMillis();   // ENDED LOADING

        System.out.println( String.format("loading data took %f seconds", (end-start) / 1000.0 ) );

        start = System.currentTimeMillis(); // START CALCULATING DEVIATION
        Map<String, DeviationModel> deviationModels = new ConcurrentHashMap<>();

        for(Integer id : userRatings.keySet()) {
            Parallel.For(userRatings.keySet(), pid -> {
                if(id.intValue() != pid.intValue()) {
                    DeviationModel z = calculateDeviation(pid, id, getRatings(userRatings, pid, id));
                    if (z.getRaters() < 2) {
                        deviationModels.put(
                                id + "-" + pid,  // Use as key in map, commented for list
                                z
                        );
                    }
                }
            });
        }

        end = System.currentTimeMillis();  // ENDED CALCULATING DEVIATION

        System.out.println( String.format("calculating divs took %f seconds", (end-start) / 1000.0 ) );


    }

    /**
     *
     * @param data          = The data set containing all the user and their ratings
     * @param itemIdA       = The id of item A
     * @param itemIdB       = The id of item B
     * @return
     *          A list of {@link Tuple}'s the ratings of one user for both item A and item B
     */
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
