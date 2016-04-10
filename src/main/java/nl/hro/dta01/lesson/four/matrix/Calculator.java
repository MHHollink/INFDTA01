package nl.hro.dta01.lesson.four.matrix;

import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.User;
import nl.hro.dta01.lesson.four.model.Tuple;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.sort;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.predictRating;

/**
 * The Calculator that predicts the ratings for a user.
 */
public class Calculator {

    /**
     * Method that is used to calculate the whole prediction for OneItem
     * @param itemId        = the item that will be predicted
     * @param targetUser    = the target user with his ratings
     * @param deviationsModels    = list of all deviations
     * @return
     *          double prediction for specified item
     */
    public static double calculate(int itemId, User targetUser, Map<String, DeviationModel> deviationsModels) {

        List<DeviationModel> deviations = new ArrayList<>();
        List<Tuple<Integer, Double>> ratings = targetUser.getRatings();

        long s = System.currentTimeMillis();

        deviations.addAll(deviationsModels.keySet().stream().filter(item ->
                Integer.parseInt(item.split("-")[0]) == (itemId) &&
                        targetUser.hasRated(Integer.parseInt(item.split("-")[1])))
                .map(deviationsModels::get)
                .collect(Collectors.toList()));

        double prediction = predictRating(ratings, deviations);

        long e = System.currentTimeMillis();
        System.out.println(
                String.format(
                        "Calculating prediction for item %d took a total of %.2f seconds, predicted a rating of %.5f",
                        itemId,
                        (e - s) / 1000.0,
                        prediction
                )
        );

        return prediction;
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
                for (int j = 0; j < predictions.size(); j++){
                    if (predictions.get(j).getB() < p){
                        System.out.println(
                                String.format(
                                        "prediction for item %d with rating %.5f got substituted for item %d",
                                        predictions.get(j).getA(),
                                        predictions.get(j).getB(),
                                        i
                                )
                        );
                        predictions.set(j, new Tuple<>(i, p));
                        Collections.sort(predictions, (a, b) -> a.getB() > b.getB() ? 1 : -1);
                        break;
                    }
                }
            }
            else
                predictions.add(new Tuple<>(i,p));
        }
        Collections.reverse(predictions);
        return predictions;
    }

}
