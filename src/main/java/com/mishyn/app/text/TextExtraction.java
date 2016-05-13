package com.mishyn.app.text;

import com.mishyn.app.db.MongoWorker;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Volodymyr on 11.05.2016.
 */

public class TextExtraction {
    private static String pathToTXT = "source.txt";
    private static String pathToStopWords = "stopwordsI.txt";
    private static final Pattern UNDESIRABLES = Pattern.compile("[^a-zA-Z ]");
    private List<String> stopWords = new ArrayList<String>();

    private static String removeUseless(String x) {
        return UNDESIRABLES.matcher(x).replaceAll("");
    }

    public List<String> extractStopWords() {
        List<String> stopWords = new ArrayList<String>();
        try {
            FileInputStream fileStream = new FileInputStream(pathToStopWords);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                stopWords.add(strLine);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setStopWords(stopWords);

        persistStopWords();
        return stopWords;
    }

    public void persistStopWords() {
        MongoWorker mv = new MongoWorker("stop");
        MongoCollection collection = mv.getMongoCollection();
        collection.drop();
        List<String> extractedWordsList = this.getStopWords();
        int countPersisted = 0;
        for (String word : extractedWordsList) {
            Document doc = new Document().append("word", word);
            collection.insertOne(doc);
            countPersisted++;
        }
    }

    public List<String> processDocument(String document) {
        List<String> sentences = TextAnalysis.splitTextSentencesLPNLP(document);
        List<String> result = new ArrayList<String>();
        // MyMongoInterface mongo = new MyMongoImpl();
        List<String> stopWords = this.getStopWords();
        int ind = 0;
        for (String sentence : sentences) {
          //  System.out.println("n-" + ind + "=" + sentence);
            String resultSentence = " " + removeUseless(sentence.toLowerCase()) + " ";
            for (String stopword : stopWords) {
                stopword = " " + stopword + " ";
                resultSentence = resultSentence.replaceAll("(?i)" + stopword, " ");
            }
            result.add(resultSentence.trim());
           // System.out.println("n-" + ind + "=" + resultSentence.trim());
            ind++;
        }
        return result;
    }

    public List<ExtractedDocument> extractDocuments() {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(pathToTXT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fileStream != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));

        String strLine;
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
                        extractedDocumentsList.add(new ExtractedDocument(documentText, documentIndex, processDocument((documentText))));
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
        persistDocuments(extractedDocumentsList);
        return extractedDocumentsList;
    }

    public void persistDocuments(List<ExtractedDocument> extractedDocumentList) {
        MongoWorker mv = new MongoWorker("documents");
        MongoCollection collection = mv.getMongoCollection();
        collection.drop();
        //  List<ExtractedDocument> extractedDocumentList = extractDocumentsFromFile();
        int countPersisted = 0;
        for (ExtractedDocument ed : extractedDocumentList) {
            Document doc = new Document();
            doc.append("text", ed.getText());
            doc.append("counter", ed.getId());
            int index = 0;
            List<Document> linesDocuments = new ArrayList<Document>();
            for (String line : ed.getLines()) {
                linesDocuments.add(new Document().append(String.valueOf(index), line));
                index++;
            }
            doc.append("lines", linesDocuments);

            // .append("cleared", ed.getWithoutSWText());
            collection.insertOne(doc);
            countPersisted++;
        }
        System.out.println("stored " + countPersisted + " documents");
    }

    public List<String> getStopWords() {
        return this.stopWords;
    }

    public void setStopWords(List<String> stopWords) {
        this.stopWords = stopWords;
    }
}
