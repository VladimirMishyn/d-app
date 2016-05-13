package com.mishyn.app.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr on 13.05.2016.
 */
public class EntityDocInfo {
    private Integer id;
    private List<String> ln = new ArrayList<String>();

    public EntityDocInfo() {
    }

    public EntityDocInfo(Integer id, List<String> ln) {
        this.id = id;
        this.ln = ln;
    }

    public void addLine(String newLineNumber) {
        this.getLn().add(newLineNumber);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getLn() {
        return ln;
    }

    public void setLn(List<String> ln) {
        this.ln = ln;
    }

    @Override
    public String toString() {
        return "EntityDocInfo{" +
                "id=" + id +
                ", ln=" + ln +
                '}';
    }
}
