package project.ontime.kz.ontime.screen.main;

import android.support.v4.app.Fragment;

import java.util.List;

import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.utils.dialog.LoadingView;

/**
 * Created by Andrey on 5/5/2017.
 */

public interface MainView extends LoadingView{

    void initWidget();

    void transactionFragment(Fragment fragment);

    void initAdapter(List<CubeSide> cubeSides);

    void openSettingActivity();

    void finishBackPress();

    void showToast();

    void identifyTypeFigure(int typeFigure);

    void openStatisticActivity();

    void finishActivity();
}
