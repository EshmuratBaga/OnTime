package project.ontime.kz.ontime.screen.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmResults;
import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.model.CubeSide;

/**
 * Created by Andrey on 4/14/2017.
 */

public class AddTaskDialog extends DialogFragment {

    private static final String SIDE_FIGURE = "side";
    private static final String TYPE_FIGURE = "type";
    private View form = null;
    private EditText etxtTitle;
    private Realm realm;
    private CubeSide cubeSide;
    private boolean isHave = false;
    int side;
    int typeFigure;

    public static AddTaskDialog newInstance(int side, int type) {
        AddTaskDialog f = new AddTaskDialog();
        Bundle args = new Bundle();
        args.putInt(SIDE_FIGURE, side);
        args.putInt(TYPE_FIGURE, type);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        side = getArguments().getInt(SIDE_FIGURE);
        typeFigure = getArguments().getInt(TYPE_FIGURE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        etxtTitle = (EditText) form.findViewById(R.id.e_txt_title_task);

        realm = Realm.getDefaultInstance();
        RealmResults<CubeSide> sides = realm.where(CubeSide.class).findAll();
        if (sides.size() != 0) {
            cubeSide = sides.where().equalTo("side", side).equalTo("type", typeFigure).findFirst();
            if (cubeSide != null) {
                etxtTitle.setText(cubeSide.getName());
                isHave = true;
            }
        } else {
            Log.d("ddddd", "size" + 0);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            if (etxtTitle.getText().toString().length() != 0){
                if (isHave) {
                    realm.beginTransaction();
                    cubeSide.setName(etxtTitle.getText().toString());
                    realm.copyToRealmOrUpdate(cubeSide);
                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    cubeSide = new CubeSide();
                    cubeSide.setId(getNextId());
                    cubeSide.setName(etxtTitle.getText().toString());
                    cubeSide.setSide(side);
                    cubeSide.setType(typeFigure);
                    realm.insert(cubeSide);
                    realm.commitTransaction();
                }
            }
            dialog.dismiss();
        });

        builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
            dialog.dismiss();
        });

        builder.setView(form).create();

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(dialog -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        });

        return alert;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private int getNextId() {
        Number currentIdNum = realm.where(CubeSide.class).max("id");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }
}
