package project.ontime.kz.ontime.screen.main.countdown;

import android.os.Bundle;

import project.ontime.kz.ontime.utils.dialog.LoadingView;

/**
 * Created by Andrey on 5/5/2017.
 */

public interface CountDownView extends LoadingView{

    void populateFeatureList();

    void initWidget(Bundle savedInstanceState);
}
