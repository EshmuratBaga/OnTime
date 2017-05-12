package project.ontime.kz.ontime.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Andrey on 4/16/2017.
 */

public class Time extends RealmObject{

    @PrimaryKey
    private int id;

    private int sideId;

    private long startTime;

    private long endTime;

    private boolean sync;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSideId() {
        return sideId;
    }

    public void setSideId(int sideId) {
        this.sideId = sideId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
