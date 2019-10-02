package com.android.batdemir.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.batdemir.library.databinding.ActivitySignUpBinding;
import com.android.batdemir.mylibrary.Tools.Tool;

public class SignUpActivity extends AppCompatActivity {

    private Context context;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView((Activity) context, R.layout.activity_sign_up);
        binding.btnLogin.setOnClickListener(v -> new Tool(context).move(LoginActivity.class, false, false, null));
    }
}