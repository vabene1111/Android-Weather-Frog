package de.droidenschmiede.weather;

import android.support.v4.app.FragmentTransaction;

public class DialogManager {

    public MainActivity m;

    public DialogManager(MainActivity m) {
        this.m = m;
    }

    public void showDonateDialog(){

        String tag = "dialogDonate";
        FragmentTransaction ft = fragTransaction(tag);
        SpendenDialogFragment dialogFrag = new SpendenDialogFragment().newInstance();
        dialogFrag.show(ft, tag);
    }

    public void showDankeDialog(){

        String tag = "dialogDanke";
        FragmentTransaction ft = fragTransaction(tag);
        DankeDialogFragment dialogFrag = new DankeDialogFragment().newInstance();
        dialogFrag.show(ft, tag);
    }

    private FragmentTransaction fragTransaction(String tag){

        android.support.v4.app.FragmentTransaction ft = m.getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment prev = m.getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        return ft;
    }
}
