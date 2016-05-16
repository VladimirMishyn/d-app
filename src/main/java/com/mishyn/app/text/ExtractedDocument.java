package com.mishyn.app.text;

import java.util.List;

/**
 * Created by Volodymyr on 11.05.2016.
 */
public class ExtractedDocument {

    private String text;
    private int id;
    private int ngramsCount;
    private List<String> lines;

    public ExtractedDocument(String text, int id, List<String> lines) {
        this.text = text;
        this.id = id;
        this.lines = lines;
    }

    public ExtractedDocument() {
        this.text = "";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public int getNgramsCount() {
        return ngramsCount;
    }

    public void setNgramsCount(int ngramsCount) {
        this.ngramsCount = ngramsCount;
    }

    @Override
    public String toString() {
        return "ExtractedDocument{" +
                "text='" + text + '\'' +
                ", id=" + id +
                ", ngramsCount=" + ngramsCount +
                ", lines=" + lines +
                '}';
    }
}
