package project.ontime.kz.ontime.screen.statistic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.st.BlueSTSDK.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.RealmResults;
import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.screen.main.CubeSideAdapter;
import project.ontime.kz.ontime.screen.main.MainActivity;
import project.ontime.kz.ontime.screen.scan.NodeContainerFragment;
import project.ontime.kz.ontime.utils.dialog.LoadingDialog;
import project.ontime.kz.ontime.utils.dialog.LoadingView;

public class StatisticActivity extends AppCompatActivity implements StatisticView {
    private LoadingView loadingView;
    private StatisticPresenter presenter;

    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<Integer> colors = new ArrayList<>();

    public static Intent getStartIntent(Context c) {
        Intent i = new Intent(c, StatisticActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return i;
    }//getStartIntent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initActionBar();
        randomColor();
        initWidget();

    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
    }

    @Override
    public void initWidget() {
        loadingView = LoadingDialog.view(getFragmentManager());
        presenter = new StatisticPresenter(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_static);
        presenter.initAdapter();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(this, getSupportFragmentManager(), colors);
        viewPager.setAdapter(adapter);

        Log.d("dddd","vipager" + viewPager.getCurrentItem());

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initAdapter(List<CubeSide> cubeSides) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new CubeSideAdapter(cubeSides, this, colors));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void randomColor() {
        for (int i = 0; i < 12; i++) {
            colors.add(random());
        }
    }

    private int random() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public void showLoading() {
        loadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        loadingView.hideLoading();
    }
}
