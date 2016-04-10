package nl.hro.dta01.lesson.four;


import nl.hro.dta01.lesson.four.matrix.Calculator;
import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.User;
import nl.hro.dta01.lesson.two.model.Tuple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static nl.hro.dta01.lesson.four.Importer.loadDataMovieLens;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.calculateDeviation;
import static nl.hro.dta01.lesson.four.Importer.loadDataUserItem;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.*;

public class Main {

    static List<Integer> itemDataSet;
    static Map<Integer, User> userRatings;
    static Map<String, DeviationModel> deviationModels;

    static long start; // used for timers
    static long end;   // used for timers

    public static void main(String[] args) throws InterruptedException {

        itemDataSet = new ArrayList<>();
        for (int i = 1; i < 1683; i++) {
        //for (int i = 101; i < 107; i++) {
            itemDataSet.add(i);
        }

        out.println( "loading data..." );
        start = System.currentTimeMillis(); // START LOADING

        userRatings = loadDataMovieLens();
        end = System.currentTimeMillis();   // ENDED LOADING
        out.println( format("loading took %f seconds", (end-start) / 1000.0 ) );

        deviationModels = new ConcurrentHashMap<>();
        out.println( "calculating..." );
        start = System.currentTimeMillis(); // START CALCULATING DEVIATION
        itemDataSet.parallelStream()
                .forEach(item -> {
                    itemDataSet.parallelStream().filter(innerItem -> innerItem.intValue() != item.intValue()).forEach(innerItem -> {
                        DeviationModel z = calculateDeviation(innerItem, item, getRatings(userRatings, innerItem, item));
                        deviationModels.put(
                                item + "-" + innerItem,  // Use as key in map, commented for list
                                z
                        );
                    });
                });
        end = System.currentTimeMillis();  // ENDED CALCULATING DEVIATION
        out.println(format("calculating took %f seconds", (end-start) / 1000.0 ) );

        /** SMALL SET **/
//        out.println(format("We predict that user %d would rate item %d with a %f rating",7,101,predictOne(7,101)));
//        out.println(format("We predict that user %d would rate item %d with a %f rating", 7,103,predictOne(7,103)));
//        out.println(format("We predict that user %d would rate item %d with a %f rating", 7,106,predictOne(7,106)));
//        out.println(format("We predict that user %d would rate item %d with a %f rating", 3,103,predictOne(3,103)));
//        out.println(format("We predict that user %d would rate item %d with a %f rating", 3,105,predictOne(3,105)));

        // TODO, update user 3 rating, recalculate user 7.

        /** BIG SET **/

        out.println( "predicting..." );
        start = currentTimeMillis();
        List< Tuple<Integer, Double>> predictions = predictList(186, 5);
        end = currentTimeMillis();
        out.println(format("predicting took %f seconds", (end-start) / 1000.0 ) );
        System.out.println(predictions);

        // Add new rating and update deviations
        Map<String,DeviationModel> newDeviations = addRating(3,103,5.0, userRatings, deviationModels, itemDataSet);
    }

    /**
     *
     * @param data          = The data set containing all the user and their ratings
     * @param itemIdA       = The id of item A
     * @param itemIdB       = The id of item B
     * @return
     *          A list of {@link Tuple}'s the ratings of one user for both item A and item B
     */
    public static List<Tuple<Double, Double>> getRatings (Map<Integer, User> data, int itemIdA, int itemIdB) {
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

    public static Map<String, DeviationModel> addRating(int userId, int itemId, double rating, Map<Integer, User> data, Map<String, DeviationModel> deviations, List<Integer> itemDataset) {
        Map<String, DeviationModel> newDeviationsModels = deviations;
        System.out.println("============= updated deviationModels ===============");

        User user = data.get(userId);
        user.addRating(itemId, rating);

        deviations.entrySet().stream().filter(entry -> ((entry.getValue().getItemIdA() == itemId) || (entry.getValue().getItemIdB() == itemId)) && user.hasRated(entry.getValue().getItemIdA()) && user.hasRated(entry.getValue().getItemIdB())).forEach(entry -> {
            List<Tuple<Double, Double>> newRatings = new ArrayList<>();
            double ratingItemA = user.getRatingForItem(entry.getValue().getItemIdA());
            double ratingItemB = user.getRatingForItem(entry.getValue().getItemIdB());

            newRatings.add(new Tuple<>(ratingItemA, ratingItemB));

            DeviationModel updatedDeviationModel = updateDeviationModel(entry.getValue(), newRatings);
            newDeviationsModels.put(entry.getKey(), updatedDeviationModel);
            System.out.println(entry.getValue().getItemIdA() + "-" + entry.getValue().getItemIdB() + ": " + updatedDeviationModel.toString());
        });
        return newDeviationsModels;
    }


    public static List<Tuple<Integer,Double>> predictList(int id, int n) {
        return Calculator.calculate(userRatings.get(id), deviationModels, itemDataSet, n);
    }

    public static double predictOne(int id, int item) {
        return Calculator.calculate(item, userRatings.get(id), deviationModels);
    }

}
