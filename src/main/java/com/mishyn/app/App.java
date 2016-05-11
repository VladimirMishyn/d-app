package com.mishyn.app;

import com.mishyn.app.db.MongoWorker;
import com.mishyn.app.text.LuceneImplementation;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) throws IOException {


        //TextWorker.persistDocuments();
//        MongoWorker mv = new MongoWorker("documents");
//        MongoCollection collection = mv.getMongoCollection();
//        FindIterable<Document> iterable = collection.find(
//                eq("counter", 100));
//        iterable.forEach(new Block<Document>() {
//            @Override
//            public void apply(Document document) {
//                System.out.println(document);
//                String text = document.getString("text");
//                LuceneImplementation.nGramsPrint(text);
//            }
//        });
        LuceneImplementation.nGramsPrint("text for test one. text test 2,");
    }
}
