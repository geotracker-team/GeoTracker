package com.juanjo.udl.geotracker.Activities.GlobalActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.juanjo.udl.geotracker.Activities.Layouts.OptionsActivity;
import com.juanjo.udl.geotracker.Management.DataManagement;
import com.juanjo.udl.geotracker.Management.NetworkManager;
import com.juanjo.udl.geotracker.R;

public class GlobalAppCompatActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private ActionBar bar;
    private NetworkManager nm;
    protected DataManagement dataManagement;

    //ActionBar
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        showActionBar();
        dataManagement = new DataManagement(this);
    }//onCreate

    //region MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.general_menu, menu);
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_options:
                Intent intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
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
    //endregion

    //region Internet
    public boolean isConnectionAllowed(){
        return nm.isConectionAllowed();
    }//isConnectionAllowed

    public boolean isConnected() { return nm.isConnected; }//isConnection

    @Override
    public void onResume(){
        super.onResume();
        if (nm == null) nm = new NetworkManager(this);
        registerReceiver(nm, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void onPause(){
        super.onPause();
        try{
            if(nm != null) unregisterReceiver(nm);
        } catch (IllegalArgumentException e) {
            nm = null;
        }
    }//onPause
    //endregion

    //region General
    public void processException(final Exception e) {
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

    protected void noConectionError(){
        showToast(getString(R.string.txtNoInternet), Toast.LENGTH_SHORT);
    }//noConection
    //endregion

    //region Dialog
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

    public void dismissDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        });
    }//dismissDialog
    //endregion
}
