package project.ontime.kz.ontime.screen.setting;

import project.ontime.kz.ontime.utils.dialog.LoadingView;

/**
 * Created by Andrey on 5/6/2017.
 */

public interface SettingView extends LoadingView{

    void initWidget();

    void showDialog();

    void populateFeatureList();

    void showAddTaskDialog(int sum);
}
