package net.iamaprogrammer.util;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class FileUtil {
    public static String readFromStream(InputStream inputStream) {
        Scanner reader = new Scanner(inputStream);
        StringBuilder output = new StringBuilder();

        while (reader.hasNextLine()) {
            output.append(reader.nextLine()).append("\n");
        }
        reader.close();
        return output.toString();
    }
}
