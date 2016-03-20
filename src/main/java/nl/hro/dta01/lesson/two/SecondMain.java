package nl.hro.dta01.lesson.two;

import com.j256.ormlite.stmt.query.In;
import nl.hro.dta01.lesson.two.importer.MovieLensDataImporter;
import nl.hro.dta01.lesson.two.importer.UserItemDataImporter;
import nl.hro.dta01.lesson.two.matrix.SimilarityMatrix;
import nl.hro.dta01.lesson.two.model.Tuple;
import nl.hro.dta01.lesson.two.model.UserPreference;

import java.util.*;

/**
 * Created by Marcel on 20-3-2016.
 */
public class SecondMain {


    /**
     * Map Containing the UserID as {@link Integer} and the ratings as {@link UserPreference}
     */
    private static Map<Integer, UserPreference> userPreferences;
    /**
     * A set of item ids which could be rated
     */
    private static Set<Integer> itemIds;

    /**
     * Default settings
     */
    public static final int DEFAULT_NUMBER_OF_NEAREST_NEIGHBOURS = 10;
    public static final int DEFAULT_TARGET_USER_ID = 186;
    public static final double DEFAULT_SIMILARITY_THRESHOLD = 0.35;
    private static final int DEFAULT_MAX_OF_PREDICTIONS = 8;

    /**
     * Entry point
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        userPreferences = MovieLensDataImporter.ImportUserItemDataIntoUserPreferences();
        itemIds = new HashSet<>();

        for (int i = 1; i < 1683; i++) {
            itemIds.add(i);
        }

        System.out.println(
                String.format("Loaded data for %d users", userPreferences.size())
        );

        // Get nearest neighbours
        List<Tuple<Integer, Double>> nearestNeighbours = getNearestNeighbours(
                DEFAULT_TARGET_USER_ID,
                DEFAULT_NUMBER_OF_NEAREST_NEIGHBOURS,
                DEFAULT_SIMILARITY_THRESHOLD
        );

        // Get the top of the predicted ratings for UN-rated Products
        List<Tuple<Integer, Double>> predictedRatings = getPredictedRatings(
                DEFAULT_TARGET_USER_ID,
                nearestNeighbours,
                DEFAULT_MAX_OF_PREDICTIONS
        );

        String output = !predictedRatings.isEmpty() ? predictedRatings.toString() : "Sorry. We could not predict any ratings for this user.";

        System.out.println(output);
    }

    private static List<Tuple<Integer, Double>> getPredictedRatings(int targetUserID, List<Tuple<Integer, Double>> nearestNeighbours, int maxPredictions) {

        List<Tuple<Integer, Double>> predictedRatings = new ArrayList<>(maxPredictions);

        for (Integer itemId : itemIds) {

            if(userPreferences.get(targetUserID).getRatings().containsKey(itemId))
                continue;


            Tuple<Integer, Double> prediction = new Tuple<>(
                    itemId,
                    getPredictedRatingForItem(
                            nearestNeighbours,
                            itemId
                    )
            );

            if(prediction.getY() == 0)
                continue;


            if (predictedRatings.size() == maxPredictions) { // If list is full
                for (int i = 0; i < maxPredictions; i++) {
                    if (prediction.getY() > predictedRatings.get(i).getY()) { // If above threshold and higher than other similarity
                        predictedRatings.set(i, prediction); // set neighbour to the new (better) neighbour
                        break;
                    }
                }
            } else { // If list is not full
                predictedRatings.add(prediction);
            }



        }

        return predictedRatings;
    }

    private static double getPredictedRatingForItem(List<Tuple<Integer, Double>> nearestNeighbours, int itemId) {

        double maxInfluence = 0;
        double prediction = 0;

        for (Tuple<Integer, Double> nearestNeighbour : nearestNeighbours) {
            maxInfluence += nearestNeighbour.getY();
        }

        for (Tuple<Integer, Double> nearestNeighbour : nearestNeighbours) {

            if ( userPreferences.get(nearestNeighbour.getX()).getRatings().get(itemId) == null ) {
                // Some data needs to be modified since a user did not rate this item
                maxInfluence -= nearestNeighbour.getY();
                continue;
            }

            double rating = userPreferences.get(nearestNeighbour.getX()).getRatings().get(itemId);
            double similarity = nearestNeighbour.getY();

            prediction += normalize(similarity,maxInfluence) * rating;
        }

        return prediction;
    }


    /**
     * Gets a set of users with their similarity in form of a {@link Tuple}
     *
     * @param targetUserID The target user of which the nearest neighbours are wanted
     * @param numberOfNearestNeighbours The size of the List of neighbours that is returned
     * @param threshold the threshold that is used to see if the neighbour is near enough for further calculation
     * @return List of UserId, Similarity
     */
    private static List<Tuple<Integer, Double>> getNearestNeighbours(int targetUserID, int numberOfNearestNeighbours, double threshold) {

        // Create list which can be filled / returned with the given cap
        List<Tuple<Integer, Double>> nearestNeighbours = new ArrayList<>(numberOfNearestNeighbours);

        // Loop over all users in the dataset
        for (Integer currentNeighbourID : userPreferences.keySet()) {

            if (currentNeighbourID == targetUserID)
                continue; // Skip your self

            Tuple<Integer, Double> currentNeighbour = // Create Tuple for the current user
                    new Tuple<>(
                            currentNeighbourID, // X = userId
                            SimilarityMatrix.getInstance()
                                    .calculateSimilarity(
                                            true, // False = Cosine, True = Pearson
                                            userPreferences.get(targetUserID),
                                            userPreferences.get(currentNeighbourID)
                                    ) // Y = similarity
                    );

            if (nearestNeighbours.size() == numberOfNearestNeighbours) { // If list is full
                for (int i = 0; i < nearestNeighbours.size(); i++) {
                    if (currentNeighbour.getY() > threshold &&
                            currentNeighbour.getY() > nearestNeighbours.get(i).getY()) { // If above threshold and higher than other similarity
                        nearestNeighbours.set(i, currentNeighbour); // set neighbour to the new (better) neighbour
                        break;
                    }
                }
            } else { // If list is not full
                nearestNeighbours.add(currentNeighbour); // Add to the list
            }

            nearestNeighbours.sort((a, b) -> (a.getY() - b.getY() > 0) ? 1 : -1); // sort the list so the lowers similarity is on top.
        }

        Collections.reverse(nearestNeighbours); // sort the list before returning so the highest similarity is on top

        return nearestNeighbours;
    }

    /**
     * function to get a cut between 0 and 1
     */
    public static double normalize(double value, double max)
    {
        return value / max;
    }
}
