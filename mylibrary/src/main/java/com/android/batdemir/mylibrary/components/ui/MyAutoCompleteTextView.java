package com.android.batdemir.mylibrary.components.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.batdemir.mylibrary.components.adapters.SpinnerAdapter;
import com.android.batdemir.mylibrary.components.helper.SpinnerHelper;
import com.android.batdemir.mylibrary.components.models.SpinnerModel;
import com.google.android.material.textview.MaterialAutoCompleteTextView;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class MyAutoCompleteTextView extends MaterialAutoCompleteTextView {

    public MyAutoCompleteTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public MyAutoCompleteTextView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MyAutoCompleteTextView(@NonNull Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init();
    }

    private void init() {
        setImeOptions(EditorInfo.IME_ACTION_DONE);
        setSingleLine(true);
    }

    private boolean getControl() {
        if (getText().toString().isEmpty())
            return false;
        if (getAdapter() == null)
            return false;
        return getAdapter().getCount() != 0;
    }

    private boolean setControl(Object item) {
        if (item == null)
            return false;
        if (getAdapter() == null)
            return false;
        return getAdapter().getCount() != 0;
    }

    private SpinnerModel getItemFromModel() {
        SpinnerAdapter spinnerAdapter = (SpinnerAdapter) getAdapter();
        SpinnerModel selectedItem = null;
        if (getControl()) {
            for (int i = 0; i < spinnerAdapter.getModels().size(); i++) {
                if (spinnerAdapter.getModels().get(i).getDescription().equals(getText().toString())) {
                    selectedItem = spinnerAdapter.getModels().get(i);
                    break;
                }
            }
        }
        return selectedItem;
    }

    //Component Fill & Clear Functions

    public void fill(@ArrayRes int arrayId) {
        setAdapter(new SpinnerAdapter(getContext(), SpinnerHelper.getInstance().cast(Arrays.asList(getResources().getStringArray(arrayId)))));
    }

    @SuppressWarnings("unchecked")
    public void fill(List<?> models) {
        if (models.isEmpty())
            return;
        if (models.get(0) instanceof SpinnerModel) {
            setAdapter(new SpinnerAdapter(getContext(), (List<SpinnerModel>) models));
        } else if (models.get(0) instanceof String) {
            setAdapter(new SpinnerAdapter(getContext(), SpinnerHelper.getInstance().cast((List<String>) models)));
        }
    }

    public void fill(List<?> models, Field id, Field description) {
        setAdapter(new SpinnerAdapter(getContext(), SpinnerHelper.getInstance().cast(models, id, description)));
    }

    public void clear() {
        setAdapter(null);
    }

    //Component Item Functions

    public SpinnerModel getSelectedItem() {
        return getItemFromModel();
    }

    public Object getSelectedItemId() {
        if (getSelectedItem() == null)
            return null;
        return getSelectedItem().getId();
    }

    public String getSelectedItemDescription() {
        if (getSelectedItem() == null)
            return null;
        return getSelectedItem().getDescription();
    }

    public Object getSelectedItemModel(Class<?> classType) {
        if (getSelectedItem() == null)
            return null;
        return new Gson().fromJson(getSelectedItem().getModel(), classType);
    }

    public boolean setSelectByItem(SpinnerModel item) {
        SpinnerAdapter spinnerAdapter = (SpinnerAdapter) getAdapter();
        if (setControl(item)) {
            for (int i = 0; i < spinnerAdapter.getModels().size(); i++) {
                if (spinnerAdapter.getModels().get(i).equals(item)) {
                    setText(spinnerAdapter.getModels().get(i).getDescription());
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean setSelectByItemId(Object itemId) {
        SpinnerAdapter spinnerAdapter = (SpinnerAdapter) getAdapter();
        if (setControl(itemId)) {
            for (int i = 0; i < spinnerAdapter.getModels().size(); i++) {
                if (spinnerAdapter.getModels().get(i).getId().equals(itemId)) {
                    setText(spinnerAdapter.getModels().get(i).getDescription());
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean setSelectByItemDescription(String itemDescription) {
        SpinnerAdapter spinnerAdapter = (SpinnerAdapter) getAdapter();
        if (setControl(itemDescription)) {
            for (int i = 0; i < spinnerAdapter.getModels().size(); i++) {
                if (spinnerAdapter.getModels().get(i).getDescription().equals(itemDescription)) {
                    setText(spinnerAdapter.getModels().get(i).getDescription());
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean setSelectByItemModel(Object itemModel) {
        SpinnerAdapter spinnerAdapter = (SpinnerAdapter) getAdapter();
        if (setControl(itemModel)) {
            String itemModelStr = new Gson().toJson(itemModel);
            for (int i = 0; i < spinnerAdapter.getModels().size(); i++) {
                if (spinnerAdapter.getModels().get(i).getModel().equals(itemModelStr)) {
                    setText(spinnerAdapter.getModels().get(i).getDescription());
                    break;
                }
            }
            return true;
        }
        return false;
    }

}
