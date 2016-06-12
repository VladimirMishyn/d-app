package com.mishyn.app;

import com.mishyn.app.csv.MatrixGeneration;
import com.mishyn.app.db.MyMongoImpl;
import com.mishyn.app.db.MyMongoInterface;
import com.mishyn.app.entity.Entity;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {

        // extracting texts and stopwords-persisting
//        TextExtraction extraction = new TextExtraction();
//        extraction.extractStopWords();
//        extraction.extractDocuments();
//        System.out.println("text and words extracted from source and persisted");


        // -Xmx6144m for this
        // extracting ngrams from text and persisting
//        MyMongoInterface mongo = new MyMongoImpl();
//        Processor processor = new Processor();
//        List<ExtractedDocument> extractedDocumentList = mongo.getAllExtractedDocuments();
//        System.out.println("text extracted from mongo");
//        processor.processIt(extractedDocumentList);


        // counting tf-idf and updating
//        MyMongoInterface mongo = new MyMongoImpl();
//        Processor processor = new Processor();
//        long docCount = mongo.getAllExtractedDocumentsCount();
//        Corpus corpus = new Corpus(mongo.getAllExtractedDocuments());
//        System.out.println("texts extracted from mongo");
//        corpus.generateInfo();
//        Map<Integer, Integer> info = corpus.getDocsGramInfo();
//        long entitiesCount = mongo.getEntitiesCount();
//        for (int i = 0; i < entitiesCount; i++) {
//            Entity entity = mongo.getEntityById(i);
//            mongo.updateEntities(entity, processor.tfIdfCount(entity, docCount, info));
//        }


        //counting dispersion
//        MyMongoInterface mongo = new MyMongoImpl();
//        List<ExtractedDocument> extractedDocumentList = mongo.getAllExtractedDocuments();
//        System.out.println("finished getting docs");
//        List<Entity> entityList = mongo.getAllEntities();
//        System.out.println("finished getting entities");
//        Processor processor = new Processor();
//        processor.dispersion(extractedDocumentList, entityList);

        //counting hgv
//        MyMongoInterface mongo = new MyMongoImpl();
//        List<ExtractedDocument> extractedDocumentList = mongo.getAllExtractedDocuments();
//        System.out.println("finished getting docs");
//        List<Entity> entityList = mongo.getAllEntities();
//        System.out.println("finished getting entities");
//        Processor processor = new Processor();
//        //for tf-idf
//        processor.HVG(extractedDocumentList, entityList, "t");
//        //for dispersion
//        processor.HVG(extractedDocumentList, entityList, "d");

        MyMongoInterface mongo = new MyMongoImpl();
        List<Entity> words = mongo.getTopEntitiesBy("1", 300, "hvgt");
        List<Entity> bigrams = mongo.getTopEntitiesBy("2", 300, "hvgt");
        List<Entity> threegrams = mongo.getTopEntitiesBy("3", 300, "hvgt");
        MatrixGeneration mg = new MatrixGeneration(words, bigrams, threegrams);
        mg.generateIt();
    }
}
