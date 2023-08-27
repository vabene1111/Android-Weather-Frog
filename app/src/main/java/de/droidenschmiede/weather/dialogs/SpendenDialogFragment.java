package de.droidenschmiede.weather.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import de.droidenschmiede.weather.MainActivity;
import de.droidenschmiede.weather.R;

public class SpendenDialogFragment extends DialogFragment {

    public MainActivity m;
    public View v;

    private LinearLayout layout;
    private ImageButton ibClose;
    private Button buttonBuy;

    //UI

    public SpendenDialogFragment() {

        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SpendenDialogFragment newInstance() {

        SpendenDialogFragment frag = new SpendenDialogFragment();
        //Bundle args = new Bundle();
        //args.putString("title", "title");
        //args.pu
        //frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setStyle(STYLE_NO_TITLE, R.style.DialogSmall);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogPremium);

        this.v = inflater.inflate(R.layout.dialog_spenden, container, false);
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

        ibClose = v.findViewById(R.id.ib_premium_close);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

//        layout = v.findViewById(R.id.layout_premium);
//        buttonBuy = v.findViewById(R.id.button_donate_buy);
//        buttonBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                m.billingManager.showPurchaseWindow();
//                dismiss();
//            }
//        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.DialogDonate);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            //int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //dialog.getWindow().setLayout(width , height);

            Window window = getDialog().getWindow();
            window.setLayout((int)(getDisplaySize().x), height);
            window.setGravity(Gravity.CENTER);
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
