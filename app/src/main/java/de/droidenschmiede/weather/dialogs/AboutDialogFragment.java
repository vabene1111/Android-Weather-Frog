package de.droidenschmiede.weather.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import de.droidenschmiede.weather.MainActivity;
import de.droidenschmiede.weather.R;

public class AboutDialogFragment extends DialogFragment {

    public MainActivity m;
    public View v;

    public AboutTyp typ;

    //UI General
    public ImageButton ibBack;
    public TextView tvToolbar;

    //UI Impressum
    public TextView tvVersion;



    public AboutDialogFragment() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AboutDialogFragment newInstance(AboutTyp typ) {

        AboutDialogFragment frag = new AboutDialogFragment();
        //Bundle args = new Bundle();
        //args.putString("title", "title");
        //args.pu
        //frag.setArguments(args);
        Bundle args = new Bundle();
        args.putInt("abouttyp", typ.ordinal());
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setStyle(STYLE_NO_TITLE, R.style.DialogSmall);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFull);

        this.v = inflater.inflate(R.layout.dialog_about, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //String title = getArguments().getString("title", "Enter Name");
        //getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        //mEditText.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
        //        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#ffff00")));
        //getDialog().getWindow().setBackgroundDrawableResource(R.color.colorAccent);

        this.m = (MainActivity) getActivity();

        typ = AboutTyp.fromInteger(getArguments().getInt("abouttyp", 0));



        //m.fbtnAdd.hide();



        //getDialog().getWindow().setBackgroundDrawable(null);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initToolbar();
        inflateSubscreen();
    }

    public void initToolbar(){

        ibBack = v.findViewById(R.id.ib_about_toolbar_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvToolbar = v.findViewById(R.id.tv_about_toolbar);
        tvToolbar.setText(getUeberschrift());

    }

    public String getUeberschrift(){

        switch (typ){
            case INFO:
                return m.getResources().getString(R.string.info_title);
            case IMPRESSUM:
                return m.getResources().getString(R.string.rechtlich_title);
        }
        return "";
    }

    public void inflateSubscreen(){

        ViewStub stub = (ViewStub) v.findViewById(R.id.stub_about);
        View inflatedSubscreen;

        switch (typ){

            case INFO:
                stub.setLayoutResource(R.layout.about_info);
                 inflatedSubscreen = stub.inflate();
                setScreenInfo();
                break;
            case IMPRESSUM:
                stub.setLayoutResource(R.layout.about_impressum);
                inflatedSubscreen = stub.inflate();
                break;

        }
    }


    public void setScreenInfo(){

        String versionString = "";
        int versionId = 0;

        try {
            PackageInfo pInfo = m.getPackageManager().getPackageInfo(m.getPackageName(), 0);
            versionString = pInfo.versionName;
            versionId = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvVersion = v.findViewById(R.id.tv_impressum_version);
        tvVersion.setText(m.getResources().getString(R.string.info_version) + " " + versionString + " (" + versionId + ")");

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogSmall);
        Dialog dialog = new Dialog(getActivity(), R.style.DialogFull);

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //dialog.show();


        //LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        //builder.setView(inflater.inflate(R.layout.dialog_add, null));
        //return builder.create();

        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();





        Dialog dialog = getDialog();
        if (dialog != null) {
            //int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            //dialog.getWindow().setLayout(width , height);

            Window window = getDialog().getWindow();
            window.setLayout((int)(getDisplaySize().x), height);
            window.setGravity(Gravity.CENTER);



            //Window window = dialog.getWindow();
            // set "origin" to top left corner, so to speak
            //window.setGravity(Gravity.TOP|Gravity.LEFT);
            // after that, setting values for x and y works "naturally"
            //WindowManager.LayoutParams params = window.getAttributes();
            //params.x = 0;
            //params.y = (int) (getDisplaySizeY() / 4f);

            //dialog.getWindow().setAttributes(params);



        }




    }


    private Point getDisplaySize(){

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        // Get the screen's density scale
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        //setSizePx(size.x, heigthDp * scale + 0.5f);
        return size;
    }

        @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        //m.fbtnAdd.show();
    }
}
