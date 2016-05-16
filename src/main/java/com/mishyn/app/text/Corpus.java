package com.mishyn.app.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Volodymyr on 16.05.2016.
 */
public class Corpus {
    private List<ExtractedDocument> extractedDocuments = new ArrayList<ExtractedDocument>();
    private Map<Integer,Integer> docsGramInfo = new HashMap<Integer, Integer>();

    public ExtractedDocument getThisDocInfo(int id) {
        for (ExtractedDocument info : this.getExtractedDocuments()) {
            if (info.getId() == id) {
                return info; //gotcha!
            }
        }
        return null;
    }

    public void generateInfo(){
        for (ExtractedDocument extractedDocument: this.getExtractedDocuments()){
            docsGramInfo.put(extractedDocument.getId(),extractedDocument.getNgramsCount());
        }
    }

    public Corpus(List<ExtractedDocument> extractedDocuments) {
        this.extractedDocuments = extractedDocuments;
    }

    public List<ExtractedDocument> getExtractedDocuments() {
        return extractedDocuments;
    }

    public void setExtractedDocuments(List<ExtractedDocument> extractedDocuments) {
        this.extractedDocuments = extractedDocuments;
    }

    public Map<Integer, Integer> getDocsGramInfo() {
        return docsGramInfo;
    }

    public void setDocsGramInfo(Map<Integer, Integer> docsGramInfo) {
        this.docsGramInfo = docsGramInfo;
    }
}
