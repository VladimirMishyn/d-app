package com.mishyn.app.db;

import com.mishyn.app.entity.Entity;
import com.mishyn.app.entity.EntityDocInfo;
import com.mishyn.app.text.ExtractedDocument;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.asList;

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
        final List<ExtractedDocument> extractedDocuments = new ArrayList<ExtractedDocument>();
        MongoWorker mv = new MongoWorker("documents");
        MongoCollection collection = mv.getMongoCollection();

        FindIterable<Document> iterable = collection.find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                ExtractedDocument ed = new ExtractedDocument();
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
                extractedDocuments.add(ed);
                //  ed.setWithoutSWText(document.getString("cleared"));
            }
        });
        return extractedDocuments;
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

    @Override
    public List<ExtractedDocument> getExtractedDocumentInRange(int start, int end) {
        final List<ExtractedDocument> extractedDocuments = new ArrayList<ExtractedDocument>();
        MongoWorker mv = new MongoWorker("documents");
        MongoCollection collection = mv.getMongoCollection();

        FindIterable<Document> iterable = collection.find(new Document("counter", new Document("$gt", start))
                .append("counter", new Document("$lt", end)));
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                ExtractedDocument ed = new ExtractedDocument();
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
                extractedDocuments.add(ed);
                //  ed.setWithoutSWText(document.getString("cleared"));
            }
        });
        return extractedDocuments;
    }

    @Override
    public long getAllExtractedDocumentsCount() {
        MongoWorker mv = new MongoWorker("documents");
        MongoCollection collection = mv.getMongoCollection();
        return collection.count();
    }

    @Override
    public void persistEntities(List<Entity> allEntities) {
        MongoWorker mv = new MongoWorker("grams");
        MongoCollection collection = mv.getMongoCollection();
        collection.drop();
        for (Entity entity : allEntities) {
            Document document = new Document();
            document.append("value", entity.getValue());
            document.append("type", entity.getType());
            document.append("absfr", entity.getAbsFr());
            List<Document> docInfo = new ArrayList<Document>();
            for (EntityDocInfo edi : entity.getDocs()) {
                Document embeddedDocInfo = new Document();
                embeddedDocInfo.append("id", edi.getId());
                embeddedDocInfo.append("lines", asList(edi.getLn().toArray()));
                docInfo.add(embeddedDocInfo);
            }
            document.append("docs", asList(docInfo.toArray()));
            collection.insertOne(document);
        }


    }

}
