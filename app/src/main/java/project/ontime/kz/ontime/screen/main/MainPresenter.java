package project.ontime.kz.ontime.screen.main;

import android.os.Handler;
import android.support.v4.app.Fragment;

import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Node;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.model.TypeFigure;

/**
 * Created by Andrey on 5/5/2017.
 */

public class MainPresenter {

    private final MainView view;
    private Realm realm;
    private RealmResults<CubeSide> cubeSides;
    private Boolean exit = false;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void transactionFragment(Fragment fragment){
        view.transactionFragment(fragment);
    }

    public void initAdapter(){
        realm = Realm.getDefaultInstance();
        cubeSides = realm.where(CubeSide.class).findAll();
        view.initAdapter(cubeSides);
    }

    public void checkDataCubeSide() {
        realm = Realm.getDefaultInstance();
        RealmResults<CubeSide> sides = realm.where(CubeSide.class).findAll();
        if (sides.size() == 0){
            view.openSettingActivity();
            view.finishActivity();
        }else {
            TypeFigure figure = realm.where(TypeFigure.class).equalTo("isUse", true).findFirst();
            view.identifyTypeFigure(figure.getId());
        }
    }

    public void backPressed() {
        if (exit) {
            view.finishBackPress();
        } else {
            exit = true;
            view.showToast();
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }

    public void disableNaotification(Node mNode) {
        if (mNode.isConnected()) {
            List<Feature> features = mNode.getFeatures();
            for (Feature f : features) {
                if (mNode.isEnableNotification(f))
                    mNode.disableNotification(f);
            }
        }//if
    }

    public void openStatisticActivity() {
        view.openStatisticActivity();
    }

    public void openSettingActivity() {
        view.openSettingActivity();
    }
}
