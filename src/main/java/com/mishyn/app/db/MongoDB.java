package com.mishyn.app.db;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
/**
 * Created by Volodymyr on 11.05.2016.
 */
public class MongoDB {
    private MongoClient mongoClient;

    public MongoDB(){
        mongoClient = new MongoClient();
    }

    public MongoDatabase getDBInstance(String name){
        return mongoClient.getDatabase(name);
    }

}
