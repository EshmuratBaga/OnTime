package project.ontime.kz.ontime.screen.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.model.Time;
import project.ontime.kz.ontime.screen.statistic.StatisticActivity;


/**
 * Created by Andrey on 4/15/2017.
 */

public class CubeSideAdapter extends RecyclerView.Adapter<CubeSideAdapter.CubeHolder> {

    private List<CubeSide> cubeSides = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();
    private Context context;
    private long spendtime = 0;
    private TextDrawable myDrawable;
    private boolean isStatistic;

    public CubeSideAdapter() {
    }

    public CubeSideAdapter(List<CubeSide> cubeSides, Context context) {
        this.cubeSides = cubeSides;
        this.context = context;
    }

    public CubeSideAdapter(List<CubeSide> cubeSides, Context context, List<Integer> colors) {
        this.cubeSides = cubeSides;
        this.context = context;
        this.colors = colors;
        isStatistic = true;
    }

    public class CubeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView txtTitle;
        private TextView txtTime;

        public CubeHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img_cube_side_item);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_cube_side_title);
//            txtTime = (TextView) itemView.findViewById(R.id.txt_cube_side_time);
        }

        @Override
        public void onClick(View v) {
            if (isStatistic){
                switch (v.getId()){
                    case R.id.img_cube_side_item:

                        break;
                }
            }
        }
    }

    @Override
    public CubeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_cube_side, parent, false);
        return new CubeHolder(view);
    }

    @Override
    public void onBindViewHolder(CubeHolder holder, int position) {
        if (colors.size() != 0){
            myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(cubeSides.get(position).getName().substring(0, 1), colors.get(position));
        }else {
            myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(cubeSides.get(position).getName().substring(0, 1), randomColor());
        }
        holder.imageView.setImageDrawable(myDrawable);
        holder.txtTitle.setText(cubeSides.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cubeSides.size();
    }

    private int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
