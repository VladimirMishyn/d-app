package com.mishyn.app.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Volodymyr on 08.06.2016.
 */
public class GenerateCSV {
    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";

    public static void saveCSVForParams(String name, List<String> header, List<List<String>> content) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter( name + ".csv");
            for (String head : header) {
                fileWriter.append(DELIMITER);
                fileWriter.append(head);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (List<String> row : content) {
                int lastIndex = row.size() - 1;
                int index = 0;
                for (String column : row) {
                    fileWriter.append(column);
                    if (index != lastIndex) {
                        fileWriter.append(DELIMITER);
                    }
                    index++;
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }


}
