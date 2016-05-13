package com.mishyn.app;

import com.mishyn.app.db.MyMongoImpl;
import com.mishyn.app.db.MyMongoInterface;
import com.mishyn.app.entity.process.Processor;
import com.mishyn.app.text.ExtractedDocument;
import com.mishyn.app.text.TextExtraction;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
//        TextExtraction extraction = new TextExtraction();
//        extraction.extractStopWords();
//        extraction.extractDocuments();

//        //TextExtraction.persistDocuments();
//        //  TextExtraction.persistStopWords();


//          MyMongoInterface mongo = new MyMongoImpl();
//          ExtractedDocument ed1 = mongo.getExtractedDocumentById(0);
//          extraction.processDocument(ed1.getText());
//        ExtractedDocument ed2 = mongo.getExtractedDocumentById(200);


//        List<ExtractedDocument> extractedDocumentList = mongo.getAllExtractedDocuments();
//        extractedDocumentList.add(ed1);
//        extractedDocumentList.add(ed2);


        // -Xmx6144m for this
//        MyMongoInterface mongo = new MyMongoImpl();
//        List<ExtractedDocument> extractedDocumentList = mongo.getAllExtractedDocuments();
//        Processor processor = new Processor();
//        processor.processIt(extractedDocumentList);

//        LuceneImplementation lImp = new LuceneImplementation();
//        for (String sentence : TextAnalysis.splitTextSentencesSNLP(ed.getWithoutSWText())) {
//            lImp.nGramsPrint(sentence);
//        }
    }
}
