package com.android.batdemir.library.ui.ui_default.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.batdemir.library.R;
import com.android.batdemir.library.databinding.ActivityBottomNavigationBinding;
import com.android.batdemir.library.ui.base.BaseActivity;
import com.android.batdemir.mylibrary.tools.Tool;

public class BottomNavigationActivity extends BaseActivity {

    private Context context;
    private ActivityBottomNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(false, true, "bottomNavigation", 0f, R.style.AppThemeActionBar);

    }

    @Override
    public void getObjectReferences() {
        context = this;
        if (binding == null) {
            binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
        }
    }

    @Override
    public void loadData() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController((Activity) context, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) context, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public void setListeners() {
        binding.fab.setOnClickListener(v -> Tool.getInstance(context).move(TabbedActivity.class, true, true, null));
    }
}
