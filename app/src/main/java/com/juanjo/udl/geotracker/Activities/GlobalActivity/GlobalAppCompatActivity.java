package com.juanjo.udl.geotracker.Activities.GlobalActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.juanjo.udl.geotracker.R;

public class GlobalAppCompatActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private ActionBar bar;

    //ActionBar
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        showActionBar();
    }//onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//onOptionsItemSelected

    protected void showActionBar(){
        bar = getSupportActionBar();

        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle("");
        }
    }//showActionBar

    protected void hideActionBar(){
        if(bar != null) bar.hide();
    }//hideActionBar

    protected void setActionBartTitle(String title){
        if(bar != null) {
            bar.setTitle(title);
        }
    }//setActionBartTitle

    //General
    protected void processException (final Exception e) {
        showToast(e.getMessage(), Toast.LENGTH_LONG);
        dismissDialog();
    }//processException

    protected void showToast(final String text, final int type){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, type).show();
            }
        });
    }//showToast

    //Dialog
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
