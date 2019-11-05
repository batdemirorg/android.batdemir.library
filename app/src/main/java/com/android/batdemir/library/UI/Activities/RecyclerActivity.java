package com.android.batdemir.library.UI.Activities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.android.batdemir.library.App.Base.BaseActivity;
import com.android.batdemir.library.UI.Adapters.AdapterRecyclerView;
import com.android.batdemir.library.Api.CallTest;
import com.android.batdemir.library.Models.Player;
import com.android.batdemir.library.R;
import com.android.batdemir.library.databinding.ActivityRecyclerBinding;
import com.android.batdemir.mylibrary.API.Connect;
import com.android.batdemir.mylibrary.API.ConnectServiceErrorListener;
import com.android.batdemir.mylibrary.API.ConnectServiceListener;
import com.android.batdemir.mylibrary.API.RetrofitClient;
import com.android.batdemir.mylibrary.Components.Dialog.MyAlertDialog;
import com.android.batdemir.mylibrary.Components.Dialog.MyAlertDialogListener;
import com.android.batdemir.mylibrary.Tools.SwipeTools.SwipeController;
import com.android.batdemir.mylibrary.Tools.SwipeTools.SwipeControllerActions;
import com.android.batdemir.mylibrary.Tools.Tool;

import java.util.ArrayList;

import retrofit2.Response;

public class RecyclerActivity extends BaseActivity implements
        MyAlertDialogListener,
        ConnectServiceListener,
        ConnectServiceErrorListener {

    //EXAMPLE OF RECYCLER VIEW WITH SWIPE
    private Context context;
    private ActivityRecyclerBinding binding;

    @Override
    public void getObjectReferences() {
        init(false, true, "RecyclerView", 16f);
        context = this;
        binding = DataBindingUtil.setContentView((Activity) context, R.layout.activity_recycler);
    }

    @Override
    public void loadData() {
        setupRecyclerView();
    }

    @Override
    public void setListeners() {
        binding.btnPreviousPage.setOnClickListener(v -> new Tool(context).move(MainActivity.class, false, false, null));

        binding.btnConnectService.setOnClickListener(v -> {
            RetrofitClient.setBaseUrl("http://192.168.1.30/api/");
            new Connect().connect(context, RetrofitClient.getInstance().create(CallTest.class).callTest("2019", "ss", false), "Test");
        });
    }

    private void setupRecyclerView() {
        ArrayList<Player> players = new ArrayList<>();
        String name = "Batuhan";
        String nationality = "Turkey";
        String club = "BatSport";

        for (int i = 0; i < 20; i++) {
            players.add(i, new Player(name + players.size(), nationality, club, (9 + i), (40 - i)));
        }

        AdapterRecyclerView adapterRecyclerView = new AdapterRecyclerView(context, players);
        binding.recyclerListView.setAdapter(adapterRecyclerView);
        binding.recyclerListView.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false));
        binding.recyclerListView.setItemViewCacheSize(players.size());
        binding.recyclerListView.setHasFixedSize(true);

        SwipeController swipeControllerDelete = new SwipeController(true, getDrawable(android.R.drawable.ic_menu_delete), new SwipeControllerActions() {
            @Override
            public void onLeftSwiped(int position, View rootView, String message, View.OnClickListener onClickListener) {
                Player player = adapterRecyclerView.getModels().get(position);
                adapterRecyclerView.getModels().remove(player);
                binding.recyclerListView.setAdapter(new AdapterRecyclerView(context, adapterRecyclerView.getModels()));
                binding.recyclerListView.smoothScrollToPosition(position);
                String str = "Deleted \n";
                str += "\tItem Name: " + player.getName();
                str += "\tItem Age: " + player.getAge().toString();
                str += "\tItem Rating: " + player.getRating().toString();
                super.onLeftSwiped(position, binding.rootRecyclerListView, str, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterRecyclerView.getModels().add(position, player);
                        binding.recyclerListView.setAdapter(new AdapterRecyclerView(context, adapterRecyclerView.getModels()));
                        binding.recyclerListView.smoothScrollToPosition(position);
                    }
                });
            }
        });
        SwipeController swipeControllerEdit = new SwipeController(false, getDrawable(android.R.drawable.ic_menu_edit), new SwipeControllerActions() {
            @Override
            public void onRightSwiped(int position, View rootView, String message, View.OnClickListener onClickListener) {
                Player player = new Player("newBatdemir", "Turkey", "BatSpor", 100, 28);
                adapterRecyclerView.getModels().add(adapterRecyclerView.getItemCount(), player);
                binding.recyclerListView.setAdapter(new AdapterRecyclerView(context, adapterRecyclerView.getModels()));
                binding.recyclerListView.smoothScrollToPosition(adapterRecyclerView.getItemCount());
                String str = "Inserted \n";
                str += "\tItem Name: " + player.getName();
                str += "\tItem Age: " + player.getAge().toString();
                str += "\tItem Rating: " + player.getRating().toString();
                super.onRightSwiped(position, binding.rootRecyclerListView, str, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterRecyclerView.getModels().remove(player);
                        binding.recyclerListView.setAdapter(new AdapterRecyclerView(context, adapterRecyclerView.getModels()));
                        binding.recyclerListView.smoothScrollToPosition(position);
                    }
                });
            }
        });

        ItemTouchHelper itemTouchHelperDelete = new ItemTouchHelper(swipeControllerDelete);
        itemTouchHelperDelete.attachToRecyclerView(binding.recyclerListView);

        ItemTouchHelper itemTouchHelperEdit = new ItemTouchHelper(swipeControllerEdit);
        itemTouchHelperEdit.attachToRecyclerView(binding.recyclerListView);
    }

    @Override
    public void dialogOk(MyAlertDialog myAlertDialog) {
        Log.d("TAG", myAlertDialog.getTag());
        Log.d("EdiText", myAlertDialog.getEditText().getText().toString());
        Log.d("TextView", myAlertDialog.getMessage());
    }

    @Override
    public void dialogCancel(MyAlertDialog myAlertDialog) {
    }

    @Override
    public void onSuccess(String operationType, Response response) {
        MyAlertDialog.getInstance("onSuccess", MyAlertDialog.DialogStyle.ACTION).show(getSupportFragmentManager(), "success");
    }

    @Override
    public void onFailure(String operationType, Response response) {
        MyAlertDialog.getInstance("onFailure", MyAlertDialog.DialogStyle.ACTION).show(getSupportFragmentManager(), "failure");
    }

    @Override
    public void onException(String operationType, String errorMessage) {
        Log.v("test", "test");
    }
}
