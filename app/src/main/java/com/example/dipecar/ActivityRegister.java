package com.example.dipecar;

import android.app.ProgressDialog;
import android.content.Context;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.dipecar.api.client;
import com.example.dipecar.model.model_user.DataUser;
import com.example.dipecar.model.model_user.ResponseRegister;
import com.example.dipecar.myinterface.InitComponent;
import com.example.dipecar.utils.validate;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRegister extends AppCompatActivity implements InitComponent, View.OnClickListener{

    //declare component
    private EditText etNama;
    private EditText etNik;
    private EditText etUsername;
    private EditText etNumber;
    private EditText etAlamat;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Character JK;
    private RadioButton rbl;
    private RadioButton rbp;
    private Button btnRegister;
    private CoordinatorLayout coordinatorLayout;

    //declare context
    private Context mContext;

    //declare variable
    private DataUser userData;

    //declare sweet alert
   // private SweetAlertDialog pDialog;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        getSupportActionBar().hide();
    }

    @Override
    public void initUI() {
        etNama=(EditText)findViewById(R.id.et_nama);
        etNik=(EditText)findViewById(R.id.et_nik);
        etEmail=(EditText)findViewById(R.id.et_email);
        etNumber=(EditText)findViewById(R.id.et_no_telp);
        etAlamat=(EditText)findViewById(R.id.et_alamat);
        etUsername=(EditText)findViewById(R.id.et_username);
        etPassword=(EditText)findViewById(R.id.et_password);
        etConfirmPassword=(EditText)findViewById(R.id.et_confirm_password);
        rbl=(RadioButton)findViewById(R.id.jkl);
        rbp=(RadioButton)findViewById(R.id.jkp);
        btnRegister=(Button)findViewById(R.id.btn_register);

    }

    @Override
    public void initValue() {

    }

    @Override
    public void initEvent() {
        btnRegister.setOnClickListener(this);
        rbl.setOnClickListener(this);
        rbp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_register) {
            if (validasi())
                register();
        } else if (id == R.id.jkl) {
            JK = 'L';
            rbp.setChecked(false);
        } else if (id == R.id.jkp) {
            JK = 'P';
            rbl.setChecked(false);
        }
    }

    private void register(){
        pDialog = new ProgressDialog(this);
       // pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<ResponseRegister> register;
        register = client.getApi().userRegister(etNama.getText().toString(),
                                                etNik.getText().toString(),
                                                etUsername.getText().toString(),
                                                etEmail.getText().toString(),
                                                etNumber.getText().toString(),
                                                JK,
                                                etAlamat.getText().toString(),
                                                etPassword.getText().toString(),
                                                1,2);

        register.enqueue(new Callback<ResponseRegister>() {

            @Override
            public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                pDialog.hide();
                if (response.isSuccessful()){
                    if (response.body().getStatus()) {
                        Toasty.success(mContext,"Berhasil Dibuat", Toast.LENGTH_LONG).show();
                    }else {
                        Toasty.success(mContext,"Gagal dibuat", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toasty.error(mContext,"Gagal dibuat", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRegister> call, Throwable t) {
                pDialog.hide();
                Toasty.success(mContext,"Koneksi bermasalah", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Boolean validasi(){
        if (!validate.cek(etNama)
                &&!validate.cek(etNik)
                &&!validate.cek(etUsername)
                &&!validate.cek(etEmail)
         &&!validate.cek(etNumber)
         &&!validate.cek(etAlamat)
         &&!validate.cek(etPassword)
         &&!validate.cek(etConfirmPassword)) {
            if (validate.cekPassword(etConfirmPassword,etPassword.getText().toString(),etConfirmPassword.getText().toString())){
                return false;
            }else{
                return true;
            }
        } else{ return false; }
    }
}
