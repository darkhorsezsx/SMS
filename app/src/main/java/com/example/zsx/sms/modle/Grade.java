package com.example.zsx.sms.modle;

/**
 * Created by zsx on 2015/5/9.
 */
public class Grade {
    String name;
    String id;
    int credit;
    int count;
    int great;
    int normal;
    int fail;
    float greatRate;
    float normalRate;
    float failRate;

    public Grade(String name, String id, int credit, int count, int great, int normal, int fail) {
        this.name = name;
        this.id = id;
        this.credit = credit;
        this.count = count;
        this.great = great;
        this.normal = normal;
        this.fail = fail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getGreat() {
        return great;
    }

    public void setGreat(int great) {
        this.great = great;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public float getGreatRate() {
        setGreatRate();
        return greatRate;
    }

    public void setGreatRate() {
        greatRate = great/count;
    }

    public float getNormalRate() {
        setNormalRate();
        return normalRate;
    }

    public void setNormalRate() {
        normalRate = normal/count;
    }

    public float getFailRate() {
        setFailRate();
        return failRate;
    }

    public void setFailRate() {
        failRate = fail/count;
    }
}
