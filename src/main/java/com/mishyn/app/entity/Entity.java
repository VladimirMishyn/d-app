package com.mishyn.app.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Volodymyr on 13.05.2016.
 */
public class Entity {
    private Integer id;
    private String value;
    private String type;
    private long absFr;
    private List<EntityDocInfo> docs = new ArrayList<EntityDocInfo>();

    public Entity() {
    }

    public Entity(String value, String type, long absFr, List<EntityDocInfo> docs) {
        this.value = value;
        this.type = type;
        this.absFr = absFr;
        this.docs = docs;
    }

    public EntityDocInfo getThisDocInfo(Integer id) {
        for (EntityDocInfo info : this.getDocs()) {
            if (info.getId().equals(id)) {
                return info; //gotcha!
            }
        }
        return null;
    }

    public void addDocInfo(EntityDocInfo entityDocInfo) {
        this.docs.add(entityDocInfo);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAbsFr() {
        return absFr;
    }

    public void setAbsFr(long absFr) {
        this.absFr = absFr;
    }

    public List<EntityDocInfo> getDocs() {
        return docs;
    }

    public void setDocs(List<EntityDocInfo> docs) {
        this.docs = docs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Entity(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", absFr=" + absFr +
                ", docs=" + docs +
                '}';
    }
}
