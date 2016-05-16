package com.mishyn.app;

import com.mishyn.app.db.MyMongoImpl;
import com.mishyn.app.db.MyMongoInterface;
import com.mishyn.app.entity.Entity;
import com.mishyn.app.entity.process.Processor;
import com.mishyn.app.text.Corpus;
import com.mishyn.app.text.ExtractedDocument;
import com.mishyn.app.text.TextExtraction;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException {
        TextExtraction extraction = new TextExtraction();
        extraction.extractStopWords();
        extraction.extractDocuments();


        // -Xmx6144m for this
        MyMongoInterface mongo = new MyMongoImpl();
        Processor processor = new Processor();
        List<ExtractedDocument> extractedDocumentList = mongo.getAllExtractedDocuments();
        processor.processIt(extractedDocumentList);

        long docCount = mongo.getAllExtractedDocumentsCount();
        Corpus corpus = new Corpus(mongo.getAllExtractedDocuments());
        corpus.generateInfo();
        Map<Integer, Integer> info = corpus.getDocsGramInfo();
        long entitiesCount = mongo.getEntitiesCount();
        for (int i = 0; i < entitiesCount; i++) {
            Entity entity = mongo.getEntityById(i);
            mongo.updateEntities(entity, processor.tfIdfCount(entity, docCount, info));
        }



//        LuceneImplementation lImp = new LuceneImplementation();
//        for (String sentence : TextAnalysis.splitTextSentencesSNLP(ed.getWithoutSWText())) {
//            lImp.nGramsPrint(sentence);
//        }
    }
}
