package project.ontime.kz.ontime.screen.main;

import android.app.Activity;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.st.BlueSTSDK.Feature;

import io.realm.Realm;
import io.realm.RealmResults;
import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.model.Time;
import project.ontime.kz.ontime.screen.main.countup.CountUpFragment;

import static project.ontime.kz.ontime.R.id.container;

/**
 * Created by Andrey on 5/7/2017.
 */

public class SideUpdate implements Feature.FeatureListener {

    private Activity activity;
    private int typeFigure;
    private TextView txtTitleTask;
    private Chronometer chronometer;
    private ImageButton iBtnStop;
    private View view;

    private double[][] doda_sides = new double[][]{
            {0, -0.525731, 0.850651},
            {0.850651, 0, 0.525731},
            {0.850651, 0, -0.525731},
            {-0.850651, 0, -0.525731},
            {-0.850651, 0, 0.525731},
            {-0.525731, 0.850651, 0},
            {0.525731, 0.850651, 0},
            {0.525731, -0.850651, 0},
            {-0.525731, -0.850651, 0},
            {0, -0.525731, -0.850651},
            {0, 0.525731, -0.850651},
            {0, 0.525731, 0.850651},
    };

    private float[][] cube_sides = new float[][]{
            {-1, 0, 0},
            {0, -1, 0},
            {0, 0, -1},
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1},
    };

    private int curent = 0;

    private boolean px = false, py = false, pz = false;

    private double dx, dy, dz;

    private double ax = 2000, ay = 2000, az = 2000;
    private double dt = 1;
    private int sx = 0, sy = 0, sz = 0;

    private Time time;
    private Realm realm;
    private CubeSide cubeSide;
    private int side;

    public SideUpdate(Activity activity, int typeFigure, View view) {
        Log.d("dddd","constructorSideUpdate");
        this.activity = activity;
        this.typeFigure = typeFigure;
        this.view = view;

        initWidget();
    }


    public void initWidget() {
        Log.d("dddd","initwidget");
        realm = Realm.getDefaultInstance();
        txtTitleTask = (TextView) view.findViewById(R.id.txt_task_title);
        chronometer = (Chronometer) view.findViewById(R.id.chrono);
    }

    @Override
    public void onUpdate(Feature f, Feature.Sample sample) {
        final Number[] numXYZ = f.getSample().data;
        activity.runOnUiThread(() -> {
            dx = (numXYZ[0].floatValue() - ax) / dt;
            ax = numXYZ[0].floatValue();
            if (Math.abs(dx) < 50) {
                sx = sx + 1;
            } else {
                sx = 0;
            }


            dy = (numXYZ[1].floatValue() - ay) / dt;
            ay = numXYZ[1].floatValue();
            if (Math.abs(dy) < 50) {
                sy = sy + 1;
            } else {
                sy = 0;
            }


            dz = (numXYZ[2].floatValue() - az) / dt;
            az = numXYZ[2].floatValue();
            if (Math.abs(dz) < 50) {
                sz = sz + 1;
            } else {
                sz = 0;
            }

            if (sx > 5) {
                px = true;
            } else {
                px = false;
            }

            if (sy > 5) {
                py = true;
            } else {
                py = false;
            }

            if (sz > 5) {
                pz = true;
            } else {
                pz = false;
            }

            if (px && py && pz) {
//                Log.d("dddd", "" + SideUpdate.this.side(numXYZ[0].floatValue(), numXYZ[1].floatValue(), numXYZ[2].floatValue()));
                for (int i = 0; i < 12; i++) {
                    if (SideUpdate.this.side(numXYZ[0].floatValue(), numXYZ[1].floatValue(), numXYZ[2].floatValue()) == i && curent != i) {
                        curent = i;
                        cubeSide = realm.where(CubeSide.class).equalTo("side",i).findFirst();
                        setEndTimeModel(side);
                        txtTitleTask.setText(cubeSide.getName());
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        setStratTimeModel();
                        side = i;
                    }
                }
            }
        });
    }//update

    int side(float Ax, float Ay, float Az) {
        double largest_dot = 0;
        int closest_side = -1; // will return -1 in case of a zero A vector
        if (typeFigure == 1) {
            for (int side = 0; side < 6; side++) {
                double dot = (cube_sides[side][0] * Ax) +
                        (cube_sides[side][1] * Ay) +
                        (cube_sides[side][2] * Az);
                if (dot > largest_dot) {
                    largest_dot = dot;
                    closest_side = side;
                }
            }
        }
        if (typeFigure == 3) {
            for (int side = 0; side < 12; side++) {
                double dot = (doda_sides[side][0] * Ax) +
                        (doda_sides[side][1] * Ay) +
                        (doda_sides[side][2] * Az);
                if (dot > largest_dot) {
                    largest_dot = dot;
                    closest_side = side;
                }
            }
        }

        return closest_side;
    }

    public void setStratTimeModel(){
        time = new Time();
        time.setId((int) (1 + System.currentTimeMillis()));
        time.setSideId(cubeSide.getId());
        time.setStartTime(System.currentTimeMillis());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(time);
        realm.commitTransaction();
    }

    public void setEndTimeModel(int side){
        CubeSide faceSide = realm.where(CubeSide.class).equalTo("side", side).findFirst();
        if (realm.where(Time.class).equalTo("sideId", faceSide.getId()).findAll().size() != 0) {
            RealmResults<Time> realmResults = realm.where(Time.class).equalTo("sideId", faceSide.getId()).findAll();
            time = realmResults.last();
            if (time.getEndTime() == 0) {
                realm.beginTransaction();
                time.setEndTime(System.currentTimeMillis());
                realm.copyToRealmOrUpdate(time);
                realm.commitTransaction();
            }
        }
    }
}
