package com.android.batdemir.library.ui.base;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.android.batdemir.library.R;
import com.android.batdemir.mylibrary.components.dialog.MyAlertDialog;
import com.android.batdemir.mylibrary.components.dialog.MyAlertDialogListener;

import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity implements
        BaseActions,
        MyAlertDialogListener {

    private boolean isFirstActivity;

    @Override
    protected void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run() {
                runOnUiThread(() -> getObjectReferences());
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    loadData();
                    setListeners();
                });
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isFirstActivity) {
            MyAlertDialog.getInstance(getString(R.string.alert_dialog_key_exit), MyAlertDialog.DialogStyle.ACTION).show(getSupportFragmentManager(), getString(R.string.alert_dialog_key_exit));
            return;
        } else {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dialogOk(MyAlertDialog myAlertDialog) {
        if (Objects.equals(myAlertDialog.getTag(), getString(R.string.alert_dialog_key_exit))) {
            finishAffinity();
            System.exit(0);
        }
    }

    @Override
    public void dialogCancel(MyAlertDialog myAlertDialog) {
        if (Objects.equals(myAlertDialog.getTag(), getString(R.string.alert_dialog_key_exit))) {
            myAlertDialog.dismiss();
        }
    }

    public void init(boolean isFirstActivity, boolean showHomeButton, String title, float elevation) {
        this.isFirstActivity = isFirstActivity;
        setTheme(R.style.AppThemeActionBar);
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar();
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setElevation(elevation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeButton);
    }

    public void init(boolean isFirstActivity, boolean showHomeButton, String title, float elevation, int theme) {
        this.isFirstActivity = isFirstActivity;
        setTheme(theme);
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar();
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setElevation(elevation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeButton);
    }
}
