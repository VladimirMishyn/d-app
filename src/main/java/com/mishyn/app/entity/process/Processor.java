package com.mishyn.app.entity.process;

import com.mishyn.app.db.MyMongoImpl;
import com.mishyn.app.db.MyMongoInterface;
import com.mishyn.app.entity.Entity;
import com.mishyn.app.entity.EntityDocInfo;
import com.mishyn.app.text.ExtractedDocument;
import com.mishyn.app.text.LuceneImplementation;

import java.io.FileNotFoundException;
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

    private double tfCount(Entity entity, Map<Integer, Integer> info) {
        double tf = 0;
        for (EntityDocInfo entityDocInfo : entity.getDocs()) {
            Integer infoValue = info.get(entityDocInfo.getId());
            if (infoValue != null) {
                tf += (double) entityDocInfo.getLn().size() / info.get(entityDocInfo.getId());
            } else {
                System.out.println("bp");
            }
        }
        return tf;
    }

    private double idfCount(Entity entity, long docCount) {
        List<EntityDocInfo> entityDocInfoList = entity.getDocs();
        double ngramApproximateCount = docCount / 3;
        return Math.log(1 + ngramApproximateCount / (double) entityDocInfoList.size());
    }

    public double tfIdfCount(Entity entity, long docCount, Map<Integer, Integer> info) {
        return tfCount(entity, info) * idfCount(entity, docCount);
    }

    public void processIt(List<ExtractedDocument> readyDocuments) {
        //   List<ExtractedDocument> readyDocuments = mongo.getAllExtractedDocuments();
        Map<String, Entity> entityMap = new HashMap<String, Entity>();
        long hashMapSize = 0;
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

    public void countForDoc(List<String> ngrams, Map<String, Entity> entityMap, Map<String, Integer> countMap) {
        Map<Integer, String> index = new HashMap<Integer, String>();
        Map<String, Double> tfIdf = new HashMap<String, Double>();
        int k = 0;
        for (String s : ngrams) {
            index.put(k, s);
            tfIdf.put(s, entityMap.get(s).getTfidf());
            k++;
        }

        for (int i = 0; i < ngrams.size() - 1; i++) {
            String base = index.get(i);
            double baseTfIdf = tfIdf.get(base);
            double toCheckWithBase = 0;
            double visibilityMax = 0;
            int m = i + 1;
            while (toCheckWithBase < baseTfIdf) {
                String currentEntity = index.get(m);
                if (currentEntity != null) {
                    toCheckWithBase = tfIdf.get(currentEntity);
                    if (toCheckWithBase > visibilityMax) {
                        visibilityMax = toCheckWithBase;
                        Integer counterOfCurrent = countMap.get(currentEntity);
                        Integer countOfBase = countMap.get(base);
                        countMap.put(currentEntity, new Integer(counterOfCurrent.intValue() + 1));
                        countMap.put(base, new Integer(countOfBase.intValue() + 1));
                    }
                } else {
                    break;
                }
                m++;
            }
        }
    }

    public void HVG(List<ExtractedDocument> extractedDocumentList, List<Entity> entityList) throws FileNotFoundException {
        Map<String, Entity> entityMap = new HashMap<String, Entity>();
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        System.out.println("base initiation");
        for (Entity entity : entityList) {
            entityMap.put(entity.getValue(), entity);
            countMap.put(entity.getValue(), 0);
        }
        System.out.println("base initiation finish");
        for (ExtractedDocument ed : extractedDocumentList) {
            List<String> ngrams = new ArrayList<String>();
            for (String line : ed.getLines()) {
                ngrams.addAll(lucene.getNgrams(line));
            }
            List<String> words = new ArrayList<String>();
            List<String> bigram = new ArrayList<String>();
            List<String> threegram = new ArrayList<String>();
            for (String ngram : ngrams) {
                String[] splited = ngram.split("\\s+");
                switch (splited.length) {
                    case 1:
                        words.add(ngram);
                        break;
                    case 2:
                        bigram.add(ngram);
                        break;
                    case 3:
                        threegram.add(ngram);
                        break;
                }
            }
            countForDoc(words, entityMap, countMap);
            countForDoc(bigram, entityMap, countMap);
            countForDoc(threegram, entityMap, countMap);
        }
        System.out.println("counted-persisting");
        for (Entity e : entityList) {
            mongo.updateHVG(e, countMap.get(e.getValue()));
        }
    }
}
