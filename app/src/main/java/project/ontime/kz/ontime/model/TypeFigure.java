package project.ontime.kz.ontime.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Andrey on 5/6/2017.
 */

public class TypeFigure extends RealmObject {

    @PrimaryKey
    private int id;

    private int fcount;

    private String name;

    private boolean isUse = false;

    public TypeFigure() {
    }

    public TypeFigure(int id, int fcount, String name) {
        this.id = id;
        this.fcount = fcount;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }
}
