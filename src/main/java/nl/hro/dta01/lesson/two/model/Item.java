package nl.hro.dta01.lesson.two.model;

import nl.hro.dta01.lesson.two.model.enums.Genre;

import java.util.HashSet;

public class Item {

    public static class Column {
        public static final String ID = "item_id";
        public static final String NAME = "title";
        public static final String REALEASE_DATE = "release_date";
        public static final String URL = "imdb_url";
        public static final String GENRES = "genres";
    }

    private Integer id;
    private String name;
    private String date;
    private String imdbUrl;
    private HashSet<Genre> genres;

}
