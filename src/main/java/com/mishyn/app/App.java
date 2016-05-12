package com.mishyn.app;

import com.mishyn.app.db.MyMongoImpl;
import com.mishyn.app.db.MyMongoInterface;
import com.mishyn.app.text.ExtractedDocument;
import com.mishyn.app.text.LuceneImplementation;
import com.mishyn.app.text.TextAnalysis;
import com.mishyn.app.text.TextExtraction;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
//        TextExtraction extraction = new TextExtraction();
//        extraction.extractStopWords();
//        extraction.extractDocuments();

//        //TextExtraction.persistDocuments();
//        //  TextExtraction.persistStopWords();
        MyMongoInterface mongo = new MyMongoImpl();
        ExtractedDocument ed = mongo.getExtractedDocumentById(100);
        System.out.println(ed);
//        TextAnalysis.splitTextSentencesSNLP(ed.getWithoutSWText());
//        TextAnalysis.splitTextSentencesBI(ed.getWithoutSWText());
//        TextAnalysis.splitTextSentencesLPNLP(ed.getWithoutSWText());
//        LuceneImplementation lImp = new LuceneImplementation();
//        for (String sentence : TextAnalysis.splitTextSentencesSNLP(ed.getWithoutSWText())) {
//            lImp.nGramsPrint(sentence);
//        }
    }
}
