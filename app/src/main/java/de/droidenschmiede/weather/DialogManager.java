package de.droidenschmiede.weather;

import android.support.v4.app.FragmentTransaction;

import de.droidenschmiede.weather.dialogs.AboutDialogFragment;
import de.droidenschmiede.weather.dialogs.AboutTyp;
import de.droidenschmiede.weather.dialogs.DankeDialogFragment;
import de.droidenschmiede.weather.dialogs.SpendenDialogFragment;

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

    public void showAboutDialog(AboutTyp typ){

        android.support.v4.app.FragmentTransaction ft = m.getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment prev = m.getSupportFragmentManager().findFragmentByTag("dialogAbout");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        AboutDialogFragment aboutDialogFragment = new AboutDialogFragment().newInstance(typ);
        //addDialogFragment.setTargetFragment(fragmentStart, 300);
        //addDialogFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogSmall);
        aboutDialogFragment.show(ft, "dialogAbout");
    }
}
