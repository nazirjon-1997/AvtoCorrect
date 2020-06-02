package com.developer.avtocorrectapp;

public class Slovar {
    int id;
    String ru, en, created_at, updated_at;

    public Slovar(int id, String ru, String en, String created_at, String updated_at) {
        this.id = id;
        this.ru = ru;
        this.en = en;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getRu() {
        return ru;
    }

    public String getEn() {
        return en;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
