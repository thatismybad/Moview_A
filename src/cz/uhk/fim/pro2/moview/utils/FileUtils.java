package cz.uhk.fim.pro2.moview.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    private static final String FAVORITE_MOVIES = "movies.txt";

    public static void saveStringToFile(String data) throws IOException {
        File file = new File(FAVORITE_MOVIES);
        FileWriter writer = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(String.format("%s;", data));
        bufferedWriter.close();
        writer.close();
    }

    public static String readStringFromFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

}
