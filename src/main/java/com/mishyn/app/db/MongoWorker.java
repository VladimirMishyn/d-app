package com.mishyn.app.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * Created by Volodymyr on 11.05.2016.
 */
public class MongoWorker extends MongoDB {
    private static String dbName = "diploma";
    private MongoDatabase db;
    private MongoCollection mongoCollection;

    public MongoWorker(String collectionName) {
        super();
        db = super.getDBInstance(dbName);
        if (collectionExists(collectionName)) {
            mongoCollection = db.getCollection(collectionName);
        } else {
            db.createCollection(collectionName);
            mongoCollection = db.getCollection(collectionName);
        }
    }

    public boolean collectionExists(final String collectionName) {
        MongoIterable<String> collectionNames = db.listCollectionNames();
        for (final String name : collectionNames) {
            if (name.equalsIgnoreCase(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public MongoCollection getMongoCollection() {
        return mongoCollection;
    }
}
