
import nl.hro.dta01.lesson.four.Importer;
import nl.hro.dta01.lesson.four.Main;
import nl.hro.dta01.lesson.four.model.DeviationModel;
import nl.hro.dta01.lesson.four.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.currentTimeMillis;
import static nl.hro.dta01.lesson.four.Importer.loadDataUserItem;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.*;
import static nl.hro.dta01.lesson.four.matrix.Calculator.*;

public class Tests {

    List<Integer> itemDataSet;
    Map<Integer, User> dataUserItem;
    Map<String, DeviationModel> deviations;

    @Before
    public void setUp() throws Exception {
        itemDataSet = new ArrayList<>();
        //for (int i = 1; i < 1682; i++) {
        for (int i = 101; i < 106; i++) {
            itemDataSet.add(i);
        }

        long start; // used for timers
        long end;   // used for timers

        start = currentTimeMillis(); // START LOADING
        dataUserItem = loadDataUserItem();
        end = currentTimeMillis();   // ENDED LOADING
        System.out.println(String.format("loading data took %f seconds", (end - start) / 1000.0));

        deviations = new ConcurrentHashMap<>();
        start = currentTimeMillis(); // START CALCULATING DEVIATION
        itemDataSet.parallelStream()
                .forEach(item -> {
                    itemDataSet.parallelStream()
                            .filter(innerItem -> innerItem.intValue() != item.intValue())
                            .forEach(innerItem -> {
                                DeviationModel z = calculateDeviation(innerItem, item, Main.getRatings(dataUserItem, innerItem, item));
                                if (z.getRaters() != 0) {
                                    deviations.put(
                                            item+"-"+innerItem,
                                            z
                                    );
                                }
                            });
                });
        end = currentTimeMillis();  // ENDED CALCULATING DEVIATION
        System.out.println(String.format("calculating divs took %f seconds", (end - start) / 1000.0));
    }

    @Test
    public void testName() throws Exception {

    }
}
