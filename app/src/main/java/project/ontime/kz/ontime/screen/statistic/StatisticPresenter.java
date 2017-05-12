package project.ontime.kz.ontime.screen.statistic;

import io.realm.Realm;
import io.realm.RealmResults;
import project.ontime.kz.ontime.model.CubeSide;

/**
 * Created by Andrey on 5/9/2017.
 */

public class StatisticPresenter {
    private Realm realm;
    private RealmResults<CubeSide> cubeSides;

    private final StatisticView view;

    public StatisticPresenter(StatisticView view) {
        this.view = view;
    }

    public void initAdapter() {
        realm = Realm.getDefaultInstance();
        cubeSides = realm.where(CubeSide.class).findAll();
        view.initAdapter(cubeSides);
    }
}
