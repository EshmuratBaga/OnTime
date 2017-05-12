package project.ontime.kz.ontime.screen.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.List;

import io.realm.Realm;
import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.screen.main.countdown.CountDownFragment;
import project.ontime.kz.ontime.screen.main.countup.CountUpFragment;
import project.ontime.kz.ontime.screen.scan.NodeContainerFragment;
import project.ontime.kz.ontime.screen.setting.SettingActivity;
import project.ontime.kz.ontime.screen.statistic.StatisticActivity;
import project.ontime.kz.ontime.utils.dialog.LoadingDialog;
import project.ontime.kz.ontime.utils.dialog.LoadingView;

public class MainActivity extends AppCompatActivity implements MainView{

    private final static String NODE_FRAGMENT = MainActivity.class.getCanonicalName() + "" +
            ".NODE_FRAGMENT";

    private final static String NODE_TAG = MainActivity.class.getCanonicalName() + "" +
            ".NODE_TAG";

    private Node mNode;
    private String nodeTag;

    private LoadingView loadingView;
    private MainPresenter presenter;
    private RecyclerView recyclerView;

    private int typeFigure;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_infinity:
                    presenter.disableNaotification(mNode);
                    presenter.transactionFragment(CountUpFragment.getInstance(mNode,typeFigure));
                    return true;
                case R.id.navigation_not_infinity:
                    presenter.disableNaotification(mNode);
                    presenter.transactionFragment(CountDownFragment.getInstance(mNode,typeFigure));
                    return true;
            }
            return false;
        }
    };


    public static Intent getStartIntent(Context c, @NonNull Node node) {
        Intent i = new Intent(c, MainActivity.class);
        i.putExtra(NODE_TAG, node.getTag());
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return i;
    }//getStartIntent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nodeTag = getIntent().getStringExtra(NODE_TAG);
        mNode = Manager.getSharedInstance().getNodeWithTag(nodeTag);

        initWidget();
    }

    @Override
    public void initWidget() {
        loadingView = LoadingDialog.view(getFragmentManager());

        presenter = new MainPresenter(this);
        presenter.checkDataCubeSide();
        presenter.transactionFragment(CountUpFragment.getInstance(mNode,typeFigure));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_tasks);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        presenter.initAdapter();
    }

    @Override
    public void transactionFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content,fragment);
        ft.commit();
    }

    @Override
    public void initAdapter(List<CubeSide> cubeSides) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new CubeSideAdapter(cubeSides,this));
    }

    @Override
    public void openSettingActivity() {
        Intent intent = SettingActivity.getStartIntent(this,mNode);
        startActivity(intent);
        finish();
    }

    @Override
    public void openStatisticActivity() {
        Intent intent = StatisticActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    public void finishBackPress() {
        this.finish();
    }

    @Override
    public void showToast() {
        Toast.makeText(this, "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void identifyTypeFigure(int typeFigure) {
        this.typeFigure = typeFigure;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.backPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_static) {
            presenter.openStatisticActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
