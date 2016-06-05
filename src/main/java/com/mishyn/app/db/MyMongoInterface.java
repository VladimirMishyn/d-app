package com.mishyn.app.db;

import com.mishyn.app.entity.Entity;
import com.mishyn.app.text.ExtractedDocument;

import java.util.List;

/**
 * Created by Volodymyr on 12.05.2016.
 */
public interface MyMongoInterface {
    public List<String> getAllStopWords();

    public List<String> getAllTexts();

    public List<ExtractedDocument> getAllExtractedDocuments();

    public ExtractedDocument getExtractedDocumentById(int id);

    public Entity getEntityById(int id);

    public List<Entity> getAllEntities();

    public long getEntitiesCount();

    public List<ExtractedDocument> getExtractedDocumentInRange(int start, int end);

    public long getAllExtractedDocumentsCount();

    public void persistEntities(List<Entity> allEntities);

    public void updateEntities(Entity entity, double tfidf);

    public void updateHVG(Entity key, Integer hvg);
}
