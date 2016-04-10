package nl.hro.dta01.lesson.four.matrix;

import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.User;
import nl.hro.dta01.lesson.two.model.Tuple;

import java.util.List;
import java.util.Map;

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
     */
    public static double calculate(int itemId, User targetUser, Map<String, DeviationModel> deviations) {



        return 0;
    }

    /**
     * Method to calculate the top precitions for a single user
     * @param targetUser    = The target user with his ratings
     * @param deviations    = The dataset of all deviations
     * @param max           = The max amount of prections from the top of the list. ( 5 = highest 5 predictions
     * @return
     *          list of tuple's containing item id en predicted rating
     */
    public static List<Tuple<Integer, Double>> calculate(User targetUser, Map<String, DeviationModel> deviations, int max) {

        return null;
    }

}
