package com.mishyn.app.text;

/**
 * Created by Volodymyr on 11.05.2016.
 */
public class ExtractedDocument {

    private String text;
    private int id;

    public ExtractedDocument(String text, int id) {
        this.text = text;
        this.id = id;
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

    @Override
    public String toString() {
        return "ExtractedDocument{" +
                "text='" + text + '\'' +
                ", id=" + id +
                '}';
    }
}
