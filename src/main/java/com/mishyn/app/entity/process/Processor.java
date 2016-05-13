package com.mishyn.app.entity.process;

import com.mishyn.app.db.MyMongoImpl;
import com.mishyn.app.db.MyMongoInterface;
import com.mishyn.app.entity.Entity;
import com.mishyn.app.entity.EntityDocInfo;
import com.mishyn.app.text.ExtractedDocument;
import com.mishyn.app.text.LuceneImplementation;

import java.util.*;

/**
 * Created by Volodymyr on 13.05.2016.
 */
public class Processor {

    private MyMongoInterface mongo = new MyMongoImpl();
    private LuceneImplementation lucene = new LuceneImplementation();

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void processIt(List<ExtractedDocument> readyDocuments) {
        //   List<ExtractedDocument> readyDocuments = mongo.getAllExtractedDocuments();
        Map<String, Entity> entityMap = new HashMap<String, Entity>();
        long hashMapSize =0;
        for (ExtractedDocument ed : readyDocuments) {
            int lineNumber = 0;
            for (String line : ed.getLines()) {
                List<String> ngrams = lucene.getNgrams(line);
                for (String ngram : ngrams) {
                    Entity value = entityMap.get(ngram);
                    if (value != null) {
                        value.setAbsFr(value.getAbsFr() + 1);
                        EntityDocInfo createdDocInfo = value.getThisDocInfo(ed.getId());
                        if (createdDocInfo != null) {
                            createdDocInfo.addLine(String.valueOf(lineNumber));
                        } else {
                            EntityDocInfo entityDocInfo = new EntityDocInfo();
                            entityDocInfo.setId(ed.getId());
                            entityDocInfo.addLine(String.valueOf(lineNumber));
                            value.addDocInfo(entityDocInfo);
                        }
                    } else {
                        Entity entity = new Entity();
                        EntityDocInfo entityDocInfo = new EntityDocInfo();
                        entityDocInfo.setId(ed.getId());
                        entityDocInfo.addLine(String.valueOf(lineNumber));

                        entity.setValue(ngram);
                        entity.addDocInfo(entityDocInfo);
                        entity.setAbsFr(1);

                        String[] splited = ngram.split("\\s+");
                        entity.setType(String.valueOf(splited.length));
                        entityMap.put(ngram, entity);
                        hashMapSize++;
                    }
                }
                lineNumber++;
            }
        }
      // printMap(entityMap);
        List<Entity> list = new ArrayList<Entity>(entityMap.values());
        mongo.persistEntities(list);
    }
}
