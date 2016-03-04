package nl.hro.dta01.lesson.two.model;

import java.util.HashMap;
import java.util.Map;

public class UserPreference {

    Map<String, Double> ratings;

    public UserPreference() {
        ratings = new HashMap<>();
    }

    public Map<String, Double> getRatings() {
        return ratings;
    }

    public void put(String key, double value) {
        ratings.put(key, value);
    }
}
