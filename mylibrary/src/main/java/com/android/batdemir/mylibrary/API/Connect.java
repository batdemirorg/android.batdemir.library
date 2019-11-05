package com.android.batdemir.mylibrary.API;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.android.batdemir.mylibrary.Components.Dialog.MyAlertDialog;
import com.android.batdemir.mylibrary.Tools.ToolConnection;

import retrofit2.Call;

public class Connect {

    private String noConnectionMessage = "Lütfen, Internet Bağlantınızı Kontrol Ediniz.";

    public Connect() {
    }

    public void setNoConnectionMessage(String noConnectionMessage) {
        this.noConnectionMessage = noConnectionMessage;
    }

    public void connect(Context context, Call call, String operationType) {
        if (!ToolConnection.isConnected(context)) {
            MyAlertDialog.getInstance(noConnectionMessage, MyAlertDialog.DialogStyle.INFO).show(((FragmentActivity) context).getSupportFragmentManager(), Connect.class.getSimpleName());
            return;
        }
        new ConnectService(context, operationType).execute(call);
    }
}

