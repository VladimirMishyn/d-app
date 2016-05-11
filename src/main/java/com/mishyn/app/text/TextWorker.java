package com.mishyn.app.text;

import com.mishyn.app.db.MongoWorker;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr on 11.05.2016.
 */

public class TextWorker {
    private static String pathToTXT = "source.txt";

    public static List<ExtractedDocument> extractDocumentsFromFile() {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(pathToTXT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fileStream != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));

        String strLine;
        ExtractedDocument extractedDocument;
        List<ExtractedDocument> extractedDocumentsList = new ArrayList<ExtractedDocument>();
        String documentText = "";
        int stringCount = 0;
        int documentIndex = 0;
        try {
            while ((strLine = br.readLine()) != null) {
                stringCount++;
                if (!strLine.equals("***")) {
                    documentText += strLine + " ";
                } else {
                    if (stringCount != 1) {
                        extractedDocumentsList.add(new ExtractedDocument(documentText, documentIndex));
                        documentText = "";
                        documentIndex++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractedDocumentsList;
    }

    public static void persistDocuments() {
        MongoWorker mv = new MongoWorker("documents");
        MongoCollection collection = mv.getMongoCollection();
        collection.drop();
        List<ExtractedDocument> extractedDocumentList = extractDocumentsFromFile();
        int countPersisted = 0;
        for (ExtractedDocument ed : extractedDocumentList) {
            Document doc = new Document().append("text", ed.getText())
                    .append("counter", ed.getId());
            collection.insertOne(doc);
            countPersisted++;
        }
        System.out.println("stored "+countPersisted+" documents");
    }

}
