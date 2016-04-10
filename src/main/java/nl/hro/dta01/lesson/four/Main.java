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
import static nl.hro.dta01.lesson.four.Importer.loadDataMovieLens;
import static nl.hro.dta01.lesson.four.Importer.loadDataUserItem;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.calculateDeviation;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.updateDeviationModel;

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

        System.out.println( "loading data..." );
        start = System.currentTimeMillis(); // START LOADING

        userRatings = loadDataMovieLens();
        end = System.currentTimeMillis();   // ENDED LOADING
        System.out.println( format("loading took %f seconds", (end-start) / 1000.0 ) );

        deviationModels = new ConcurrentHashMap<>();
        System.out.println( "calculating..." );
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
        System.out.println(format("calculating took %f seconds", (end-start) / 1000.0 ) );

        /** SMALL SET **/
//        System.out.println(format("We predict that user %d would rate item %d with a %f rating",7,101,predictOne(7,101)));
//        System.out.println(format("We predict that user %d would rate item %d with a %f rating", 7,103,predictOne(7,103)));
//        System.out.println(format("We predict that user %d would rate item %d with a %f rating", 7,106,predictOne(7,106)));
//        System.out.println(format("We predict that user %d would rate item %d with a %f rating", 3,103,predictOne(3,103)));
//        System.out.println(format("We predict that user %d would rate item %d with a %f rating", 3,105,predictOne(3,105)));
//
//        // Add new rating and update deviations
        deviationModels = addRating(3,105,4.0);
//
//        System.out.println(predictList(7, 3));

        /** BIG SET **/

        System.out.println( "predicting..." );
        start = System.currentTimeMillis();
        List< Tuple<Integer, Double>> predictions = predictList(186, 5);
        end = System.currentTimeMillis();
        System.out.println(format("predicting took %f seconds", (end-start) / 1000.0 ) );
        System.out.println(predictions);


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

    /**
     * adds one rating by a user and updates all the deviations
     * @param userId    = user submitted the new rating
     * @param itemId    = the item that is rated
     * @param rating    = the rating it self
     * @return  a new map containing item-item as key and a {@link DeviationModel}
     */
    public static Map<String, DeviationModel> addRating(int userId, int itemId, double rating) {
        User user = userRatings.get(userId);
        user.addRating(itemId, rating);

        deviationModels.entrySet().stream().filter(entry -> {
            if ((entry.getValue().getItemIdA() == itemId) || (entry.getValue().getItemIdB() == itemId)) {
                if (user.hasRated(entry.getValue().getItemIdA()) && user.hasRated(entry.getValue().getItemIdB())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }).forEach(entry -> {
            List<Tuple<Double, Double>> newRatings = new ArrayList<>();
            double ratingItemA = user.getRatingForItem(entry.getValue().getItemIdA());
            double ratingItemB = user.getRatingForItem(entry.getValue().getItemIdB());

            newRatings.add(new Tuple<>(ratingItemA, ratingItemB));

            DeviationModel updatedDeviationModel = updateDeviationModel(entry.getValue(), newRatings);
            deviationModels.put(entry.getKey(), updatedDeviationModel);
        });
        return deviationModels;
    }


    public static List<Tuple<Integer,Double>> predictList(int id, int n) {
        return Calculator.calculate(userRatings.get(id), deviationModels, itemDataSet, n);
    }

    public static double predictOne(int id, int item) {
        return Calculator.calculate(item, userRatings.get(id), deviationModels);
    }

}
