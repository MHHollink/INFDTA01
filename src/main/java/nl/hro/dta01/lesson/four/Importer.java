package nl.hro.dta01.lesson.four;

import nl.hro.dta01.lesson.four.model.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Importer {

    /**
     * Loading file 'userItem.data'
     *
     * @return      = Map with id of the user and a {@link User} object containing {@link User#ratings}
     */
    public static Map<Integer, User> loadDataUserItem(){
        String file;
        try {
            //noinspection ConstantConditions
            file = Importer.class.getClassLoader()
                    .getResource("userItem.data")
                    .getFile()
                    .replaceAll("%20", " ");
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }

        try {
            return readData(file,0,1,2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * Loading file 'ml-100k/u.data'
     *
     * @return      = Map with id of the user and a {@link User} object containing {@link User#ratings}
     */
    public static Map<Integer, User> loadDataMovieLens(){
        String file;
        try {
            //noinspection ConstantConditions
            file = Importer.class.getClassLoader()
                    .getResource("ml-100k/u.data")
                    .getFile()
                    .replaceAll("%20", " ");
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }

        try {
            return readData(file,0,1,2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * @param file          = String value of file path
     * @param userIds       = place in the file specified as user id
     * @param productIds    = place in the file specified as product id
     * @param ratings       = place in the file specified as ratings
     * @return              = Map with id of the user and a {@link User} object containing {@link User#ratings}
     * @throws FileNotFoundException
     */
    private static Map<Integer, User> readData(String file, int userIds, int productIds, int ratings) throws FileNotFoundException {
        Map<Integer, User> users = new HashMap<>();
        Scanner fileScanner = new Scanner(new FileReader(file));
        while (fileScanner.hasNextLine()){
            String line = fileScanner.nextLine();
            String[] lineParts = line.split(",");

            int id          = Integer.parseInt(lineParts[userIds]);
            int productId   = Integer.parseInt(lineParts[productIds]);
            double rating   = Double.parseDouble(lineParts[ratings]);

            if(!users.keySet().contains(id)) {
                User user = new User(id);
                user.addRating(productId, rating);

                users.put(id, user);
            } else {
                User user = users.get(id);
                user.addRating(productId, rating);
            }
        }
        fileScanner.close();
        return users;
    }
}
