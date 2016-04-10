package nl.hro.dta01.lesson.four.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    Integer userId;
    Map<Integer, Double> ratings;

    public User(Integer userId, Map<Integer, Double> ratings) {
        this.userId = userId;
        this.ratings = ratings;
    }

    public User(Integer userId) {
        this.userId = userId;
        ratings = new HashMap<>();
    }

    public Integer getUserId() {
        return userId;
    }

    public boolean hasRated(Integer itemId) {
        return ratings.containsKey(itemId);
    }

    public void addRating(Integer itemId, Double rating) {
        ratings.put(itemId, rating);
    }

    public double getRatingForItem(Integer itemId) {
        return ratings.get(itemId);
    }

    public List<Tuple<Integer, Double>> getRatings() {
        List<Tuple<Integer, Double>> r = new ArrayList<>();

        for (Integer item : ratings.keySet()) {
            Tuple<Integer, Double> t =
                    new Tuple<>(
                            item,
                            getRatingForItem(item)
                    );
            r.add(t);
        }

        return r;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", ratings=" + ratings +
                '}';
    }
}

