package com.juanjo.udl.geotracker.Activities.GlobalActivity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.juanjo.udl.geotracker.R;

public class GlobalAppCompatActivity extends AppCompatActivity {
    private ProgressDialog dialog;

    protected void processException (final Exception e)
    {
        showToast(e.getMessage(), Toast.LENGTH_LONG);
        dismissDialog();
    }

    protected void showToast(final String text, final int type){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, type).show();
            }
        });
    }

    protected void showDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog == null) {
                    dialog = new ProgressDialog(GlobalAppCompatActivity.this);
                    dialog.setIndeterminate(true);
                    dialog.setMessage(getString(R.string.txtLoading));
                    dialog.setCancelable(false);
                }//Create the dialog if not exists
                dialog.show();
            }
        });

    }//showDialog

    protected void dismissDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
    }//dismissDialog
}
