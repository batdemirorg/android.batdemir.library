package com.android.batdemir.mylibrary.connection;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.android.batdemir.mydialog.ui.MyAlertDialog;
import com.android.batdemir.mydialog.ui.MyDialogStyle;
import com.android.batdemir.mylibrary.R;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Response;

public class ConnectService extends AsyncTask<Call<?>, Void, Response<?>> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialog progressDialog;
    private String operationType;
    private ConnectServiceListener connectServiceListener;
    private ConnectServiceErrorListener connectServiceErrorListener;

    public ConnectService(Context context, String operationType) {
        this.context = context;
        this.operationType = operationType;
    }

    public ConnectService setConnectServiceListener(ConnectServiceListener connectServiceListener) {
        this.connectServiceListener = connectServiceListener;
        return this;
    }

    public ConnectService setConnectServiceErrorListener(ConnectServiceErrorListener connectServiceErrorListener) {
        this.connectServiceErrorListener = connectServiceErrorListener;
        return this;
    }

    @Override
    protected void onPreExecute() {
        showProgressBar();
        onPreProcess();
    }

    @Override
    protected Response<?> doInBackground(Call... calls) {
        try {
            return calls[0].execute();
        } catch (Exception e) {
            cancel(true);
            if (e.getClass().equals(ConnectException.class)) {
                new MyAlertDialog
                        .Builder()
                        .setStyle(MyDialogStyle.FAILED)
                        .setMessage(context.getString(R.string.message_connection_not_successfully_with_service) + "\n" + e.getMessage())
                        .build()
                        .show(((FragmentActivity) context).getSupportFragmentManager(), operationType);
            } else if (e.getClass().equals(SocketTimeoutException.class) || e.getClass().equals(IOException.class)) {
                new MyAlertDialog
                        .Builder()
                        .setStyle(MyDialogStyle.FAILED)
                        .setMessage(context.getString(R.string.message_connection_time_out) + "\n" + e.getMessage())
                        .build()
                        .show(((FragmentActivity) context).getSupportFragmentManager(), operationType);
            } else {
                new MyAlertDialog
                        .Builder()
                        .setStyle(MyDialogStyle.FAILED)
                        .setMessage(e.getMessage())
                        .build()
                        .show(((FragmentActivity) context).getSupportFragmentManager(), operationType);
            }
            if (connectServiceErrorListener != null)
                connectServiceErrorListener.onException(operationType, e.getMessage());
            Log.e(operationType, e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response<?> response) {
        hideProgressBar();
        onPostProcess(operationType, response);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        hideProgressBar();
    }

    //Customizable Functions

    protected void onPreProcess() {
        connectServiceListener = connectServiceListener == null ? (ConnectServiceListener) context : connectServiceListener;
        try {
            connectServiceErrorListener = connectServiceErrorListener == null ? (ConnectServiceErrorListener) context : connectServiceErrorListener;
        } catch (ClassCastException e) {
            Log.e("ConnectionErrorListener", "If you want to use this listener, main context must be implements to this listener");
        }
    }

    protected void onPostProcess(String operationType, Response<?> response) {
        if (response.isSuccessful())
            connectServiceListener.onSuccess(operationType, response);
        else
            connectServiceListener.onFailure(operationType, response);
    }

    protected void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.message_please_wait));
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
    }

    protected void hideProgressBar() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
