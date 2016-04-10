package nl.hro.dta01.lesson.four;


import nl.hro.dta01.lesson.four.matrix.Calculator;
import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.User;
import nl.hro.dta01.lesson.two.model.Tuple;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static nl.hro.dta01.lesson.four.Importer.loadDataMovieLens;
import static nl.hro.dta01.lesson.four.Importer.loadDataUserItem;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        List<Integer> itemDataSet = new ArrayList<>();
        for (int i = 1; i < 1682; i++) {
        //for (int i = 101; i < 106; i++) {
            itemDataSet.add(i);
        }

        long start; // used for timers
        long end;   // used for timers

        start = System.currentTimeMillis(); // START LOADING
        Map<Integer, User> userRatings = loadDataUserItem();
        end = System.currentTimeMillis();   // ENDED LOADING
        System.out.println( String.format("loading data took %f seconds", (end-start) / 1000.0 ) );

        Map<String, DeviationModel> deviationModels = new ConcurrentHashMap<>();
        start = System.currentTimeMillis(); // START CALCULATING DEVIATION
        itemDataSet.parallelStream()
                .forEach(item -> {
                    itemDataSet.parallelStream().filter(innerItem -> {
                        return innerItem.intValue() != item.intValue();
                    }).forEach(innerItem -> {
                        DeviationModel z = calculateDeviation(innerItem, item, getRatings(userRatings, innerItem, item));
                        if (z.getRaters() != 0) {
                            deviationModels.put(
                                    item + "-" + innerItem,  // Use as key in map, commented for list
                                    z
                            );
                            System.out.println(item + "-" + innerItem + ": " + z.toString());
                        }
                    });
                });
        end = System.currentTimeMillis();  // ENDED CALCULATING DEVIATION
        System.out.println( String.format("calculating divs took %f seconds", (end-start) / 1000.0 ) );

        System.out.println(deviationModels.size());

        Map<String,DeviationModel> newDeviations = addRating(3,103,5.0, userRatings, deviationModels, itemDataSet);

        List<Tuple<Integer, Double>> predictions = Calculator.calculate(userRatings.get(6),deviationModels, 0);

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

        deviations.entrySet().stream().filter(entry -> {
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
            newDeviationsModels.put(entry.getKey(), updatedDeviationModel);
            System.out.println(entry.getValue().getItemIdA() + "-" + entry.getValue().getItemIdB() + ": " + updatedDeviationModel.toString());
        });
        return newDeviationsModels;
    }


}
