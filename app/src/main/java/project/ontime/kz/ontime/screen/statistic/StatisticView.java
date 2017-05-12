package project.ontime.kz.ontime.screen.statistic;

import java.util.List;

import io.realm.RealmResults;
import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.utils.dialog.LoadingView;

/**
 * Created by Andrey on 5/9/2017.
 */

public interface StatisticView extends LoadingView{

    void initWidget();

    void initAdapter(List<CubeSide> cubeSides);
}
