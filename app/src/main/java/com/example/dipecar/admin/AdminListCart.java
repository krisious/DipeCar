package com.example.dipecar.admin;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dipecar.R;
import com.example.dipecar.adapter.CarsAdapter;
import com.example.dipecar.api.client;
import com.example.dipecar.model.model_mobil.DataCars;
import com.example.dipecar.model.model_mobil.ResponseCars;
import com.example.dipecar.myinterface.InitComponent;
import com.example.dipecar.utils.move;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;


import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminListCart extends Fragment implements InitComponent {
    //Declate Toolbar Tittle
    private static final String TEXT_FRAGMENT = "RENTCAR";

    //Declare Component View
    private TextView mTxtTitle;
    private View rootView;
    private RecyclerView recyclerCars;
    //Declate Activity Context
    Context mContext;

    //Declare Object Cars
    ResponseCars dataCars;
    List<DataCars> listCars=new ArrayList<>();

    //Declare Adapter
    private CarsAdapter mAdapter;

    public static AdminListCart newInstance(String text){
        AdminListCart mFragment = new AdminListCart();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mContext=getActivity();
        // TODO Auto-generated method stub
        rootView = inflater.inflate(R.layout.fragment_admin_cars, container, false);
        startInit();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_icon, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.refresh) {
            getCars();
            return true;
        } else if (itemId == R.id.add) {
            move.moveActivity(mContext, ActivityCreateMobil.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        getActivity().setTitle(getArguments().getString(TEXT_FRAGMENT));
    }

    @Override
    public void initUI() {
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
        recyclerCars = (RecyclerView)rootView.findViewById(R.id.rCarList);


    }

    @Override
    public void initValue() {
        prepareCars();
        getCars();
    }

    @Override
    public void initEvent() {

    }

    public void getCars(){
        final Call<ResponseCars> cars= client.getApi().dataMobil();
        cars.enqueue(new Callback<ResponseCars>() {
            @Override
            public void onResponse(Call<ResponseCars> call, Response<ResponseCars> response) {
                if (response.isSuccessful()) {
                    dataCars=response.body();
                    if (dataCars.getStatus()) {
                        listCars.clear();
                        listCars.addAll(dataCars.getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toasty.error(mContext, "Tidak Di Temukan Data", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toasty.error(mContext,"Tidak Di Temukan Data",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCars> call, Throwable t) {
//                Toasty.error(mContext, "Tidak Di Temukan Data", Toast.LENGTH_LONG).show();
                Toasty.error(mContext,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void prepareCars(){
        mAdapter = new CarsAdapter(listCars);
        recyclerCars.setHasFixedSize(true);
        recyclerCars.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerCars.setItemAnimator(new DefaultItemAnimator());
        recyclerCars.setAdapter(mAdapter);
        recyclerCars.setItemAnimator(new SlideLeftAlphaAnimator());
        recyclerCars.getItemAnimator().setAddDuration(500);
        recyclerCars.getItemAnimator().setRemoveDuration(500);
    }




}
