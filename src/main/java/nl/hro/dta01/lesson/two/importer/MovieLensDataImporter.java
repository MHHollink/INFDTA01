package nl.hro.dta01.lesson.two.importer;

import nl.hro.dta01.lesson.two.model.UserPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Marcel on 20-3-2016.
 */
public class MovieLensDataImporter {

    public static Map<Integer, UserPreference> ImportUserItemDataIntoUserPreferences(Map<Integer, UserPreference> userPreferences){
        String file = UserItemDataImporter.class.getClassLoader().getResource("ml-100k/u.data").getFile().replaceAll("%20"," ");
        try {
            Scanner fileScanner = new Scanner(new FileReader(file));

            while (fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();
                String[] lineParts = line.split(",");

                int userId = Integer.parseInt(lineParts[0]);
                int itemId = Integer.parseInt(lineParts[1]);
                double rating = Double.parseDouble(lineParts[2]);
                long timeStamp = Long.parseLong(lineParts[3]);

                if(!userPreferences.keySet().contains(userId)) {
                    UserPreference userPreference = new UserPreference();
                    userPreference.put(itemId, rating);

                    userPreferences.put(userId, userPreference);
                } else {
                    UserPreference userPreference = userPreferences.get(userId);
                    userPreference.put(itemId, rating);
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return userPreferences;
    }
}
