package com.mishyn.app.csv;

import com.mishyn.app.entity.Entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Volodymyr on 08.06.2016.
 */
public class MatrixGeneration {

    private List<Entity> entitiesCompiled = new ArrayList<Entity>();
    private static final List<String> localStop = new ArrayList<String>(Arrays.asList("obama", "bangladesh", "bank",
            "year", "president", "korea", "bernard", "dubai", "china", "cook", "shaik",
            "jinping", "will", "hollywoo", " comey", "people", "fy_", "barac", "office"));
    private static List<String> plusWords = new ArrayList<String>();
    private static final int MAX = 100;

    private static boolean checkString(String toCheck) {
        for (String removeIt : localStop) {
            if (toCheck.contains(removeIt)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkPlusParam(String toCheck) {
        for (String plus : plusWords) {
            if (toCheck.contains(plus)) {
                return true;
            }
        }
        return false;
    }

    private static List<Entity> removeWhiteSpaces(List<Entity> input) {
        List<Entity> result = new ArrayList<Entity>(input);
        for (Entity e : result) {
            String replaced = e.getValue().replaceAll(" ", "_");
            e.setValue(replaced);
        }
        return result;
    }

    private void generateBase(List<Entity> words) {
        for (Entity word : words) {
            plusWords.add(word.getValue());
        }
    }

    private static List<Entity> prepareEntities(List<Entity> entityList, boolean usePlus) {
        int count = 0;
        int index = 0;
        List<Entity> result = new ArrayList<Entity>();
        while (count < MAX && count <= entityList.size()) {
            Entity newEntity = entityList.get(index);
            if (checkString(newEntity.getValue())) {
                if (usePlus) {
                    if (checkPlusParam(newEntity.getValue())) {
                        result.add(newEntity);
                        count++;
                    }
                } else {
                    result.add(newEntity);
                    count++;
                }
            }
            index++;
        }
        return result;
    }

    public MatrixGeneration(List<Entity> words, List<Entity> bigrams, List<Entity> threegrams) {
        bigrams = removeWhiteSpaces(bigrams);
        threegrams = removeWhiteSpaces(threegrams);

        words = prepareEntities(words, false);
        generateBase(words);
        bigrams = prepareEntities(bigrams, true);
        threegrams = prepareEntities(threegrams, true);
        entitiesCompiled.addAll(words);
        entitiesCompiled.addAll(bigrams);
        entitiesCompiled.addAll(threegrams);
    }

    public void generateIt() {

        List<String> header = new ArrayList<String>();
        List<List<String>> matrix = new ArrayList<List<String>>();
        for (Entity base : entitiesCompiled) {
            header.add(base.getValue());
            List<String> thisLine = new ArrayList<String>();
            thisLine.add(base.getValue());
            for (Entity current : entitiesCompiled) {
                if (!current.getType().equals(base.getType())) {
                    if (current.getValue().contains(base.getValue())) {
                        thisLine.add("1");
                    } else {
                        thisLine.add("0");
                    }
                } else {
                    thisLine.add("0");
                }
            }
            matrix.add(thisLine);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
        Date date = new Date();
        String name = "length-" + MAX + "_" + dateFormat.format(date);
        GenerateCSV.saveCSVForParams(name, header, matrix);
    }

}
