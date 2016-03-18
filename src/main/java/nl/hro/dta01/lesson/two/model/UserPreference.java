package nl.hro.dta01.lesson.two.model;

import java.util.HashMap;
import java.util.Map;

public class UserPreference {

    Map<Integer, Double> ratings;

    public UserPreference() {
        ratings = new HashMap<>();
    }

    public Map<Integer, Double> getRatings() {
        return ratings;
    }

    public void put(Integer key, double value) {
        ratings.put(key, value);
    }
}
