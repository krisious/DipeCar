package com.example.dipecar.user;

import android.content.Context;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dipecar.R;
import com.example.dipecar.adapter.DetailHistoryAdapter;
import com.example.dipecar.api.client;
import com.example.dipecar.model.model_detail_transaksi.DataDetailTransaksi;
import com.example.dipecar.model.model_detail_transaksi.ResponseDetailTransaksi;
import com.example.dipecar.model.model_transaksi.DataTransaksi;
import com.example.dipecar.myinterface.InitComponent;
import com.google.gson.Gson;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;


import java.util.ArrayList;
import java.util.List;


import cn.pedant.SweetAlert.SweetAlertDialog;
import customfonts.MyTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityDetailListHistory extends AppCompatActivity implements InitComponent, View.OnClickListener {
    private MyTextView confirm;
    private Context mContext;
    private RecyclerView recyclerCart;
    //Declare Adapter
    private DetailHistoryAdapter mAdapter;
    private SweetAlertDialog pDialog;

    //Declare Object Users
    ResponseDetailTransaksi dataTransaksi;
    List<DataDetailTransaksi> listDetailTransaksi=new ArrayList<>();

    DataTransaksi transaksi;

    @Override
    protected void onCreate(Bundle SavedInstance) {
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_detail_list_history);

        Gson gson = new Gson();
        transaksi= gson.fromJson(getIntent().getStringExtra("transaksi"), DataTransaksi.class);

        mContext=this;
        startInit();
    }

    @Override
    public void startInit() {
        initToolbar();
        initUI();
        initValue();
        initEvent();
    }

    @Override
    public void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(transaksi.getKODETRANSAKSI());
    }

    @Override
    public void initUI() {
        recyclerCart = (RecyclerView)findViewById(R.id.rCartList);
    }

    @Override
    public void initValue() {
        prepareCart();
        getTransaksi();
        mAdapter.notifyDataSetChanged();
//        setCheckout();
    }

    @Override
    public void initEvent() {

    }

    private void prepareCart(){
        mAdapter = new DetailHistoryAdapter(mContext,listDetailTransaksi);
        recyclerCart.setHasFixedSize(true);
        recyclerCart.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerCart.setItemAnimator(new DefaultItemAnimator());
        recyclerCart.setAdapter(mAdapter);
        recyclerCart.setItemAnimator(new SlideLeftAlphaAnimator());
        recyclerCart.getItemAnimator().setAddDuration(500);
        recyclerCart.getItemAnimator().setRemoveDuration(500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getTransaksi(){
        final Call<ResponseDetailTransaksi> users= client.getApi().dataDetailTransaksi(transaksi.getKODETRANSAKSI());
        users.enqueue(new Callback<ResponseDetailTransaksi>() {
            @Override
            public void onResponse(Call<ResponseDetailTransaksi> call, Response<ResponseDetailTransaksi> response) {
                dataTransaksi=response.body();
                if (response.isSuccessful()) {
                    if (dataTransaksi.getStatus()) {
                        listDetailTransaksi.clear();
                        listDetailTransaksi.addAll(dataTransaksi.getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        listDetailTransaksi.clear();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(mContext, "Tidak Ada Data Ditemukan", Toast.LENGTH_LONG).show();
                    }
                }else {
                    listDetailTransaksi.clear();
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(mContext, "Tidak Ada Data Ditemukan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDetailTransaksi> call, Throwable t) {
                Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.checkout:
//                Toast.makeText(mContext,"Proses",Toast.LENGTH_LONG).show();
//                break;
        }
    }
}

