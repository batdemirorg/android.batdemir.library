package com.android.batdemir.library.api.todo.connections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.android.batdemir.library.models.todo.response.ResponseModel;
import com.android.batdemir.library.models.todo.response.ResponseStatus;
import com.android.batdemir.library.ui.ui_app.fragments.ProgressDialogFragment;
import com.android.batdemir.mydialog.ui.MyAlertDialog;
import com.android.batdemir.mydialog.ui.MyDialogStyle;
import com.android.batdemir.mylibrary.connection.ConnectService;

import java.io.IOException;

import retrofit2.Response;

public class SpecConnectService extends ConnectService {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private ProgressDialogFragment progressDialogFragment;
    private ConnectionServiceListener connectServiceListener;

    public SpecConnectService(Context context, String operationType) {
        super(context, operationType);
        this.context = context;
        this.progressDialogFragment = new ProgressDialogFragment();
    }

    @Override
    protected void onPreProcess() {
        connectServiceListener = (ConnectionServiceListener) context;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostProcess(String operationType, Response response) {
        if (response.isSuccessful()) {
            ResponseModel<Object> model = (ResponseModel<Object>) response.body();
            MyDialogStyle style;
            if (model == null)
                return;
            ResponseStatus status = getStatus(model.getStatus());
            switch (status) {
                case SUCCESS:
                    connectServiceListener.onSuccess(operationType, model.getModel());
                    style = MyDialogStyle.SUCCESS;
                    break;
                case FAIL:
                    connectServiceListener.onFailure(operationType);
                    style = MyDialogStyle.FAILED;
                    break;
                case NOT_FOUND:
                    connectServiceListener.onFailure(operationType);
                    style = MyDialogStyle.WARNING;
                    break;
                case DUPLICATE:
                    connectServiceListener.onFailure(operationType);
                    style = MyDialogStyle.INFO;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + model.getStatus());
            }
            if (model.getMessage() != null && !model.getMessage().isEmpty())
                new MyAlertDialog
                        .Builder()
                        .setStyle(style)
                        .setMessage(model.getMessage())
                        .build()
                        .show(((FragmentActivity) context).getSupportFragmentManager(), status.name());
            else {
                try {
                    connectServiceListener.onFailure(operationType);
                    new MyAlertDialog
                            .Builder()
                            .setStyle(MyDialogStyle.FAILED)
                            .setMessage(response.errorBody() == null ? "Failed" : response.errorBody().string())
                            .build()
                            .show(((FragmentActivity) context).getSupportFragmentManager(), status.name());
                } catch (IOException e) {
                    Log.e(SpecConnectService.class.getSimpleName(), e.getMessage());
                }
            }
        }
    }

    @Override
    protected void showProgressBar() {
        progressDialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "");
    }

    @Override
    protected void hideProgressBar() {
        progressDialogFragment.dismiss();
    }

    public interface ConnectionServiceListener {
        void onSuccess(String operationType, Object model);

        void onFailure(String operationType);
    }

    private ResponseStatus getStatus(int statusCode) {
        if (statusCode == 0)
            return ResponseStatus.FAIL;
        if (statusCode == 1)
            return ResponseStatus.SUCCESS;
        if (statusCode == 2)
            return ResponseStatus.DUPLICATE;
        if (statusCode == 3)
            return ResponseStatus.NOT_FOUND;
        return ResponseStatus.FAIL;
    }
}
