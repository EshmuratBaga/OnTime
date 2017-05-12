package project.ontime.kz.ontime.screen.setting;

import android.util.Log;
import android.widget.Toast;

import com.st.BlueSTSDK.Node;

import io.realm.Realm;
import io.realm.RealmResults;
import project.ontime.kz.ontime.model.TypeFigure;

/**
 * Created by Andrey on 5/6/2017.
 */

public class SettingPresenter {

    private final SettingView view;
    private Realm realm;
    private RealmResults<TypeFigure> figures;

    public SettingPresenter(SettingView view) {
        this.view = view;
    }

    public void setDatTypeFigure() {
        realm = Realm.getDefaultInstance();
        RealmResults<TypeFigure> figures = realm.where(TypeFigure.class).findAll();
        if (figures.size() == 0){
            TypeFigure cube = new TypeFigure(1,6,"cube");
            TypeFigure octa = new TypeFigure(2,8,"octa");
            TypeFigure dode = new TypeFigure(3,12,"dode");
            realm.beginTransaction();
            realm.copyToRealm(cube);
            realm.copyToRealm(octa);
            realm.copyToRealm(dode);
            realm.commitTransaction();
        }
        else {
            Log.d("dddd","typefigure is not null");
        }
    }

    public void chooseType(){
        view.showDialog();
    }

    public void initAddTask(Node mNode, Node.NodeStateListener mNodeStatusListener){
        if (mNode.isConnected()) {
            view.populateFeatureList();
        } else {
            mNode.addNodeStateListener(mNodeStatusListener);
        }
    }

    public void addTaskDialog(int side){
        view.showAddTaskDialog(side);
    }

    public void setUseFigure(int typeFigure) {
        realm = Realm.getDefaultInstance();
        figures = realm.where(TypeFigure.class).findAll();
        if (typeFigure == 1){
            realm.beginTransaction();
            figures.get(0).setUse(true);
            figures.get(1).setUse(false);
            figures.get(2).setUse(false);
            realm.copyToRealmOrUpdate(figures);
            realm.commitTransaction();
        }
        if (typeFigure == 2){
            realm.beginTransaction();
            figures.get(0).setUse(false);
            figures.get(1).setUse(true);
            figures.get(2).setUse(false);
            realm.copyToRealmOrUpdate(figures);
            realm.commitTransaction();
        }
        if (typeFigure == 3){
            realm.beginTransaction();
            figures.get(0).setUse(false);
            figures.get(1).setUse(false);
            figures.get(2).setUse(true);
            realm.copyToRealmOrUpdate(figures);
            realm.commitTransaction();
        }
    }
}
