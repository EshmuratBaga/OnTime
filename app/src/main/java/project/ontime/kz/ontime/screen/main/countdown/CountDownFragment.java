package project.ontime.kz.ontime.screen.main.countdown;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.List;

import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.screen.main.SideUpdate;
import project.ontime.kz.ontime.screen.main.countup.CountUpFragment;
import project.ontime.kz.ontime.screen.scan.NodeContainerFragment;
import project.ontime.kz.ontime.utils.dialog.LoadingDialog;
import project.ontime.kz.ontime.utils.dialog.LoadingView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountDownFragment extends Fragment implements CountDownView{

    private final static String NODE_FRAGMENT = CountUpFragment.class.getCanonicalName() + "" + ".NODE_FRAGMENT";
    private static final String COUNT_DOWN_NODE_TAG = "project.ontime.kz.ontime.screen.main.countdown";
    private static final String COUNT_DOWN_TYPE_FIGURE = "count_down_type_figure";

    private Node mNode;

    private NodeContainerFragment mNodeContainer;

    private Feature.FeatureListener mGenericUpdate;

    private Feature selectedFeature;

    private String nodeTag;

    private int typeFigure;

    private LoadingView loadingView;

    private CountDownPresenter presenter;

    public CountDownFragment() {
        // Required empty public constructor
    }

    private Node.NodeStateListener mNodeStatusListener = (node, newState, prevState) -> {
        if (newState == Node.State.Connected) {
            Log.d("dddd","NodeStateListener");
            getActivity().runOnUiThread(this::populateFeatureList);
        }
    };

    @Override
    public void populateFeatureList() {
        Log.d("dddd","populateFeatureList");
        if (mNode != null) {
            List<Feature> features = mNode.getFeatures();
            for (Feature f : features) {
                if (f.getName().contains("Accelerometer")) {
                    selectedFeature = f;
                    if (selectedFeature == null)
                        return;
                    if (mNode.isEnableNotification(selectedFeature)) {
                        Log.d("dddd","isEnableNotification");
                        selectedFeature.removeFeatureListener(mGenericUpdate);
                        mNode.disableNotification(selectedFeature);
                    } else {
                        Log.d("dddd","else");
                        //create a listener that will update the selected view
                        mGenericUpdate = new SideUpdate(getActivity(),typeFigure, getView());
                        selectedFeature.addFeatureListener(mGenericUpdate);
                        mNode.enableNotification(selectedFeature);
                    }
                }//if
            }//for
            //set the adapter as data source for the adapter
        }//if
    }


    public static CountDownFragment getInstance(@NonNull Node node, int typeFigure) {
        Bundle args = new Bundle();
        CountDownFragment fragment = new CountDownFragment();
        args.putString(COUNT_DOWN_NODE_TAG,node.getTag());
        args.putInt(COUNT_DOWN_TYPE_FIGURE,typeFigure);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("dddd","onCreateView");
        return inflater.inflate(R.layout.fragment_count_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("dddd","onViewCreated");
        initWidget(savedInstanceState);

        if (mNode.isConnected()) {
            Log.d("dddd","isConnect");
            populateFeatureList();
        } else{
            Log.d("dddd","addNodeStateListner");
            mNode.addNodeStateListener(mNodeStatusListener);
        }
    }

    @Override
    public void initWidget(Bundle savedInstanceState) {
        loadingView = LoadingDialog.view(getActivity().getFragmentManager());
        presenter = new CountDownPresenter(this);

        nodeTag = getArguments().getString(COUNT_DOWN_NODE_TAG);
        typeFigure = getArguments().getInt(COUNT_DOWN_TYPE_FIGURE);
        mNode = Manager.getSharedInstance().getNodeWithTag(nodeTag);

        if (savedInstanceState == null) {
            Intent i = getActivity().getIntent();
            mNodeContainer = new NodeContainerFragment();
            mNodeContainer.setArguments(i.getExtras());
            getActivity().getSupportFragmentManager().beginTransaction().add(mNodeContainer, NODE_FRAGMENT).commit();

        } else {
            mNodeContainer = (NodeContainerFragment) getActivity().getSupportFragmentManager().findFragmentByTag(NODE_FRAGMENT);
        }
    }

    @Override
    public void onPause() {
        Log.d("dddd","onPause");
        mNode.removeNodeStateListener(mNodeStatusListener);
        super.onPause();
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
