package nl.hro.dta01.lesson.four.matrix;

import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.User;
import nl.hro.dta01.lesson.two.model.Tuple;

import java.util.*;

import static java.util.Collections.sort;

/**
 * The Calculator that predicts the ratings for a user.
 */
public class Calculator {

    /**
     * Method that is used to calculate the whole prediction for OneItem
     * @param itemId        = the item that will be predicted
     * @param targetUser    = the target user with his ratings
     * @param deviations    = list of all deviations
     * @return
     *          double prediction for specified item
     */
    public static double calculate(int itemId, User targetUser, Map<String, DeviationModel> deviations) {

        List<DeviationModel> dev = new ArrayList<>();
        List<Tuple<Integer, Double>> ratings = targetUser.getRatings();

        for (String item : deviations.keySet()) {

            if (
                    Integer.parseInt( item.split("-")[0] ) == (itemId) &&
                    targetUser.hasRated(Integer.parseInt( item.split("-")[1] ))) {
                dev.add(deviations.get(item));
            }
        }

        sort(dev, (a, b) -> a.getItemIdA() > b.getItemIdA() ? 1 : -1);
        sort(ratings, (a, b) -> a.getA() > b.getA() ? 1 : -1);

        return SlopeOne.predictRating(
                    ratings,
                    dev
        );
    }

    /**
     * Method to calculate the top precitions for a single user
     * @param targetUser    = The target user with his ratings
     * @param deviations    = The dataset of all deviations
     * @param max           = The max amount of prections from the top of the list. ( 5 = highest 5 predictions
     * @return
     *          list of tuple's containing item id en predicted rating
     */
    public static List<Tuple<Integer, Double>> calculate(User targetUser, Map<String, DeviationModel> deviations, List<Integer> itemIds, int max) {

        List<Tuple<Integer, Double>> predictions = new ArrayList<>();

        for (Integer i : itemIds) {
            if(targetUser.hasRated(i)) continue;

            double p = calculate(i, targetUser, deviations);

            if(predictions.size() == max) {
                for (int j = 0; j < predictions.size(); j++) {
                    if(predictions.get(j).getB() < p) {
                        predictions.set(j, new Tuple<>(i,p));
                    }
                }
            } else
                predictions.add(new Tuple<>(i,p));
        }

        return predictions;
    }

}
