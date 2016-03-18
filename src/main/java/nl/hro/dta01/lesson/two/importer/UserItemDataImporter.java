package nl.hro.dta01.lesson.two.importer;

import nl.hro.dta01.lesson.two.model.UserPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;

public class UserItemDataImporter {

    public static Map<Integer, UserPreference> ImportUserItemDataIntoUserPreferences(Map<Integer, UserPreference> userPreferences){
        String file = UserItemDataImporter.class.getClassLoader().getResource("userItem.data").getFile().replaceAll("%20"," ");
        try {
            Scanner fileScanner = new Scanner(new FileReader(file));

            while (fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();
                String[] lineParts = line.split(",");

                int id = Integer.parseInt(lineParts[0]);
                int productId = Integer.parseInt(lineParts[1]);
                double rating = Double.parseDouble(lineParts[2]);

                if(!userPreferences.keySet().contains(id)) {
                    UserPreference userPreference = new UserPreference();
                    userPreference.put(productId, rating);

                    userPreferences.put(id, userPreference);
                } else {
                    UserPreference userPreference = userPreferences.get(id);
                    userPreference.put(productId, rating);
                }
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return userPreferences;
    }

}
