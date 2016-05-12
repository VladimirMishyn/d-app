package com.mishyn.app.db;

import com.mishyn.app.text.ExtractedDocument;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Volodymyr on 12.05.2016.
 */
public class MyMongoImpl implements MyMongoInterface {
    @Override
    public List<String> getAllStopWords() {
        final List<String> words = new ArrayList<String>();
        MongoWorker mv = new MongoWorker("stop");
        MongoCollection collection = mv.getMongoCollection();

        FindIterable<Document> iterable = collection.find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                words.add(document.getString("word"));
            }
        });
        return words;
    }

    @Override
    public List<String> getAllTexts() {
        return null;
    }

    @Override
    public List<ExtractedDocument> getAllExtractedDocuments() {
        return null;
    }

    @Override
    public ExtractedDocument getExtractedDocumentById(int id) {
        final ExtractedDocument ed = new ExtractedDocument();
        MongoWorker mv = new MongoWorker("documents");
        MongoCollection collection = mv.getMongoCollection();

        FindIterable<Document> iterable = collection.find(eq("counter", id));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                ed.setText(document.getString("text"));
                ed.setId(document.getInteger("counter"));
                List<Document> lines = (List<Document>) document.get("lines");
                int index = 0;
                List<String> docLines = new ArrayList<String>();
                for (Document doc : lines) {
                    docLines.add(doc.get(String.valueOf(index)).toString());
                    index++;
                }
                ed.setLines(docLines);
                //  ed.setWithoutSWText(document.getString("cleared"));
            }
        });
        return ed;
    }
}
