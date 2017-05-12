package project.ontime.kz.ontime.screen.main.countup;

import android.os.Bundle;

import project.ontime.kz.ontime.utils.dialog.LoadingView;

/**
 * Created by Andrey on 5/5/2017.
 */

public interface CountUpView extends LoadingView{

    void initWidget(Bundle savedInstanceState);
    void populateFeatureList();
}
