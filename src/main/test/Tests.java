import com.tantaman.commons.concurrent.Parallel;
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

import static nl.hro.dta01.lesson.four.Importer.loadDataUserItem;
import static nl.hro.dta01.lesson.four.matrix.SlopeOne.calculateDeviation;

public class Tests {

    List<Integer> itemDataSet;
    Map<Integer, User> dataUserItem;
    Map<String, DeviationModel> deviations;

    @Before
    public void setUp() throws Exception {


        itemDataSet = new ArrayList<Integer>(){
            {
                add(101); add(102); add(103);
                add(104); add(105); add(106);
            }
        };

        dataUserItem = loadDataUserItem();
        deviations = new ConcurrentHashMap<>();

        for(Integer id : itemDataSet) {
            Parallel.For(itemDataSet, pid -> {
                if(id.intValue() != pid.intValue()) {
                    DeviationModel z = calculateDeviation(pid, id, Main.getRatings(dataUserItem, pid, id));
                    if (z.getRaters() != 0) {
                        deviations.put(
                                id + "-" + pid,  // Use as key in map, commented for list
                                z
                        );
                    }
                }
            });
        }
    }

    @Test
    public void testName() throws Exception {

    }
}