package project.ontime.kz.ontime.screen.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.model.TypeFigure;
import project.ontime.kz.ontime.screen.main.MainActivity;
import project.ontime.kz.ontime.screen.scan.NodeContainerFragment;
import project.ontime.kz.ontime.utils.dialog.LoadingDialog;
import project.ontime.kz.ontime.utils.dialog.LoadingView;

public class SettingActivity extends AppCompatActivity implements SettingView, View.OnClickListener {

    private final static String NODE_FRAGMENT = SettingActivity.class.getCanonicalName() + "" +
            ".NODE_FRAGMENT";

    private final static String NODE_TAG = SettingActivity.class.getCanonicalName() + "" +
            ".NODE_TAG";

    /**
     * node that will stream the data
     */
    private Node mNode;

    /**
     * fragment that manage the node connection and avoid a re connection each time the activity
     * is recreated
     */
    private NodeContainerFragment mNodeContainer;

    /**
     * listener that will update the displayed feature data
     */
    private Feature.FeatureListener mGenericUpdate;

    private Feature selectedFeature;

    private String nodeTag;

    private LoadingView loadingView;
    private SettingPresenter presenter;
    private ImageButton cube;
    private ImageButton octa;
    private ImageButton dode;
    private Button button;

    private double[][] doda_sides = new double[][]{
            {0, -0.525731, 0.850651},
            {0.850651, 0, 0.525731},
            {0.850651, 0, -0.525731},
            {-0.850651, 0, -0.525731},
            {-0.850651, 0, 0.525731},
            {-0.525731, 0.850651, 0},
            {0.525731, 0.850651, 0},
            {0.525731, -0.850651, 0},
            {-0.525731, -0.850651, 0},
            {0, -0.525731, -0.850651},
            {0, 0.525731, -0.850651},
            {0, 0.525731, 0.850651},
    };

    private float[][] cube_sides = new float[][]{
            {-1, 0, 0},
            {0, -1, 0},
            {0, 0, -1},
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1},
    };

    private int curent = 0;

    private boolean px = false, py = false, pz = false;

    private double dx, dy, dz;
    private double ax = 2000, ay = 2000, az = 2000;
    private double dt = 1;
    private int sx = 0, sy = 0, sz = 0;
    private int typeFigure;

    private Node.NodeStateListener mNodeStatusListener = (node, newState, prevState) -> {
        if (newState == Node.State.Connected) {
            SettingActivity.this.runOnUiThread(this::populateFeatureList);
        }
    };

    @Override
    public void populateFeatureList() {
        if (mNode != null) {
            List<Feature> features = mNode.getFeatures();
            for (Feature f : features) {
                if (f.getName().contains("Accelerometer")) {
                    selectedFeature = f;
                    if (selectedFeature == null)
                        return;
                    if (mNode.isEnableNotification(selectedFeature)) {

                        selectedFeature.removeFeatureListener(mGenericUpdate);
                        mNode.disableNotification(selectedFeature);
                    } else {
                        //create a listener that will update the selected view
                        View view = null;
                        mGenericUpdate = new GenericFragmentUpdate((TextView) view);
                        selectedFeature.addFeatureListener(mGenericUpdate);
                        mNode.enableNotification(selectedFeature);
                    }
                }//if
            }//for

            //set the adapter as data source for the adapter

        }//if
    }

    public static Intent getStartIntent(Context c, @NonNull Node node) {
        Intent i = new Intent(c, SettingActivity.class);
        i.putExtra(NODE_TAG, node.getTag());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nodeTag = getIntent().getStringExtra(NODE_TAG);
        mNode = Manager.getSharedInstance().getNodeWithTag(nodeTag);
        initWidget();
        //create or recover the NodeContainerFragment
        if (savedInstanceState == null) {
            Intent i = getIntent();
            mNodeContainer = new NodeContainerFragment();
            mNodeContainer.setArguments(i.getExtras());

            getSupportFragmentManager().beginTransaction().add(mNodeContainer, NODE_FRAGMENT).commit();

        } else {
            mNodeContainer = (NodeContainerFragment) getSupportFragmentManager().findFragmentByTag(NODE_FRAGMENT);
        }//if-else

    }

    @Override
    public void initWidget() {
        loadingView = LoadingDialog.view(getFragmentManager());
        presenter = new SettingPresenter(this);
        presenter.setDatTypeFigure();

        cube = (ImageButton) findViewById(R.id.iBtnCube);
        octa = (ImageButton) findViewById(R.id.iBtnOcta);
        dode = (ImageButton) findViewById(R.id.iBtnDode);
        button = (Button) findViewById(R.id.btn_continue);

        cube.setOnClickListener(this);
        octa.setOnClickListener(this);
        dode.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Добавьте задачи")
                .setMessage("Переверните кубик чтобы добавить задач")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    presenter.initAddTask(mNode, mNodeStatusListener);
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void showAddTaskDialog(int side) {
        AddTaskDialog.newInstance(side, typeFigure).show(getSupportFragmentManager(), "filterdate");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iBtnCube:
                typeFigure = 1;
                presenter.setUseFigure(typeFigure);
                presenter.chooseType();
                break;
            case R.id.iBtnOcta:
                typeFigure = 2;
                presenter.setUseFigure(typeFigure);
                presenter.chooseType();
                break;
            case R.id.iBtnDode:
                typeFigure = 3;
                presenter.setUseFigure(typeFigure);
                presenter.chooseType();
                break;
            case R.id.btn_continue:
                Intent intent = MainActivity.getStartIntent(this,mNode);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void showLoading() {
        loadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        loadingView.hideLoading();
    }


    private class GenericFragmentUpdate implements Feature.FeatureListener {

        /**
         * text view that will contain the data/name
         */
        final private TextView mTextView;

        /**
         * @param text text view that will show the name/values
         */
        GenericFragmentUpdate(TextView text) {
            mTextView = text;
        }//GenericFragmentUpdate

        /**
         * set the text view text with the feature toString value
         *
         * @param f      feature that has received an update
         * @param sample new data received from the feature
         */
        @Override
        public void onUpdate(Feature f, final Feature.Sample sample) {
            final Number[] numXYZ = f.getSample().data;
            SettingActivity.this.runOnUiThread(() -> {

                dx = (numXYZ[0].floatValue() - ax) / dt;
                ax = numXYZ[0].floatValue();
                if (Math.abs(dx) < 50) {
                    sx = sx + 1;
                } else {
                    sx = 0;
                }


                dy = (numXYZ[1].floatValue() - ay) / dt;
                ay = numXYZ[1].floatValue();
                if (Math.abs(dy) < 50) {
                    sy = sy + 1;
                } else {
                    sy = 0;
                }


                dz = (numXYZ[2].floatValue() - az) / dt;
                az = numXYZ[2].floatValue();
                if (Math.abs(dz) < 50) {
                    sz = sz + 1;
                } else {
                    sz = 0;
                }

                if (sx > 5) {
                    px = true;
                } else {
                    px = false;
                }

                if (sy > 5) {
                    py = true;
                } else {
                    py = false;
                }

                if (sz > 5) {
                    pz = true;
                } else {
                    pz = false;
                }

                //************************************

                if (px && py && pz) {
                    Log.d("dddd", "" + GenericFragmentUpdate.this.side(numXYZ[0].floatValue(), numXYZ[1].floatValue(), numXYZ[2].floatValue()));
                    for (int i = 0; i < 12; i++) {
                        if (GenericFragmentUpdate.this.side(numXYZ[0].floatValue(), numXYZ[1].floatValue(), numXYZ[2].floatValue()) == i && curent != i) {
                            curent = i;
                            presenter.addTaskDialog(i);
                        }

                    }
                }
            });
        }//onUpdate

        int side(float Ax, float Ay, float Az) {
            double largest_dot = 0;
            int closest_side = -1; // will return -1 in case of a zero A vector
            if (typeFigure == 1) {
                for (int side = 0; side < 6; side++) {
                    double dot = (cube_sides[side][0] * Ax) +
                            (cube_sides[side][1] * Ay) +
                            (cube_sides[side][2] * Az);
                    if (dot > largest_dot) {
                        largest_dot = dot;
                        closest_side = side;
                    }
                }
            }
            if (typeFigure == 3) {
                for (int side = 0; side < 12; side++) {
                    double dot = (doda_sides[side][0] * Ax) +
                            (doda_sides[side][1] * Ay) +
                            (doda_sides[side][2] * Az);
                    if (dot > largest_dot) {
                        largest_dot = dot;
                        closest_side = side;
                    }
                }
            }

            return closest_side;
        }

    }//GenericFragmentUpdate
}
