package com.example.dipecar.user;

import android.app.ProgressDialog;
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
import com.example.dipecar.SPreferenced.SPref;
import com.example.dipecar.adapter.CartAdapter;
import com.example.dipecar.adapter.Carts;
import com.example.dipecar.api.client;
import com.example.dipecar.model.model_transaksi.ResponseRegisterTransaksi;
import com.example.dipecar.myinterface.InitComponent;
import com.example.dipecar.utils.move;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;
import com.pixplicity.easyprefs.library.Prefs;


import java.util.ArrayList;

import customfonts.MyTextView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityListTransaksi extends AppCompatActivity implements InitComponent, View.OnClickListener {
    private MyTextView checkout;
    private Context mContext;
    private RecyclerView recyclerCart;
    //Declare Adapter
    private CartAdapter mAdapter;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle SavedInstance) {
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_list_cart);
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
    }

    @Override
    public void initUI() {
        recyclerCart = (RecyclerView)findViewById(R.id.rCartList);
        checkout = (MyTextView)findViewById(R.id.checkout);
    }

    @Override
    public void initValue() {
        prepareCart();
        mAdapter.notifyDataSetChanged();
        setCheckout();
    }

    public void setCheckout(){
        checkout.setText("(Rp. "+String.format("%,.2f", Double.parseDouble(Carts.totalAmount(SPref.getCARTS()).toString()))+") Checkout");
    }
    @Override
    public void initEvent() {
        checkout.setOnClickListener(this);
    }

    private void prepareCart(){
        mAdapter = new CartAdapter(mContext,Carts.getOrder(SPref.getCARTS()));
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

    private void checkout(){
//        pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
        pDialog = new ProgressDialog(this);
        //  pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        // pDialog.setIndeterminate(false);
        pDialog.show();

        Call<ResponseRegisterTransaksi> checkout= client.getApi().checkout(""+Prefs.getInt(SPref.getIdUser(),0),""+Carts.totalAmount(SPref.getCARTS()),Carts.getAllOrder(SPref.getCARTS()));
        checkout.enqueue(new Callback<ResponseRegisterTransaksi>() {
            @Override
            public void onResponse(Call<ResponseRegisterTransaksi> call, Response<ResponseRegisterTransaksi> response) {
                pDialog.hide();
                if (response.isSuccessful()){
                    if (response.body().getStatus()){

                        pDialog = new ProgressDialog(ActivityListTransaksi.this);
                        //  pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setMessage("Berhasil");
                        pDialog.setCancelable(false);
                        // pDialog.setIndeterminate(false);

                        Carts.reset(SPref.getCARTS());
                                        move.moveActivity(mContext, UserMain.class);
                                        finish();
                        pDialog.show();
                        Toasty.success(mContext,"Pesanan berhasil",Toast.LENGTH_LONG).show();

//                        new ProgressDialog(mContext)
//                                .setTitle("Info?")
//                                .setTitle("Data berhasil disimpan!")
//                                .setConfirmText("Yes!")
//                                .setConfirmClickListener(new ProgressDialog().OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sDialog) {
//                                        Carts.reset(SPref.getCARTS());
//                                        move.moveActivity(mContext, UserMain.class);
//                                        finish();
//                                    }
//                                })
//                                .show();


                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRegisterTransaksi> call, Throwable t) {
                pDialog.hide();
                Toasty.error(mContext,t.getMessage().toString()+Carts.getAllOrder(SPref.getCARTS()),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.checkout) {
            if (Carts.getSize(SPref.getCARTS()) > 0)
                checkout();
            else
                Toasty.error(mContext, "Tidak ada pesanan yang diproses", Toast.LENGTH_SHORT).show();
        }
    }
}
