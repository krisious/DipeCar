package com.example.dipecar;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.dipecar.SPreferenced.SPref;
import com.example.dipecar.api.client;
import com.example.dipecar.model.model_user.DataUser;
import com.example.dipecar.model.model_user.ResponseLogin;
import com.example.dipecar.myinterface.InitComponent;
import com.example.dipecar.user.SplashActivity;
import com.example.dipecar.user.UserMain;
import com.example.dipecar.utils.move;
import com.example.dipecar.utils.validate;
import com.pixplicity.easyprefs.library.Prefs;

import cn.pedant.SweetAlert.SweetAlertDialog;
import customfonts.MyEditText;
import customfonts.MyTextView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity implements InitComponent, View.OnClickListener {

    //declare componenr
    private MyEditText et_username;
    private MyEditText et_password;
    private MyTextView btn_login;
    private MyTextView txt_register;
    private TextView logofont;
    private CoordinatorLayout coordinatorlayout;

    //declare context
    private Context mContext;

    //declate variable
    private DataUser userData;

    //declare sweet alert
 //   private SweetAlertDialog pDialog;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        startInit();
    }

    @Override
    public void startInit() {
        if (Prefs.getInt(SPref.getGroupUser(),0)==1){
            move.moveActivity(mContext, UserMain.class);
            finish();
        }
        if (Prefs.getInt(SPref.getGroupUser(),0)==2){
            move.moveActivity(mContext,UserMain.class);
            finish();
        }
        initToolbar();
        initUI();
        initValue();
        initEvent();
    }

    @Override
    public void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    public void initUI() {
        et_username=(MyEditText)findViewById(R.id.et_username);
        et_password=(MyEditText)findViewById(R.id.et_password);
        btn_login=(MyTextView)findViewById(R.id.btn_login);
        txt_register=(MyTextView) findViewById(R.id.txt_register);
        logofont=(TextView)findViewById(R.id.logofont);
        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "fonts/ArgonPERSONAL-Regular.otf");
        logofont.setTypeface(custom_fonts);
    }

    @Override
    public void initValue() {

    }

    @Override
    public void initEvent() {
        btn_login.setOnClickListener(this);
        txt_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login) {
            if (validate_login())
                login();
        } else if (id == R.id.txt_register) {
            move.moveActivity(mContext, ActivityRegister.class);
        }
    }

    public boolean validate_login(){
        return (!validate.cek(et_username)&&!validate.cek(et_password)) ? true : false;
    }

    public void login(){
        pDialog = new ProgressDialog(this);
      //  pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
       // pDialog.setIndeterminate(false);
        pDialog.show();

        Call<ResponseLogin> user = client.getApi().auth(et_username.getText().toString(),et_password.getText().toString());
        user.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                pDialog.hide();
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        userData=response.body().getData();
                        Toasty.success(mContext,"login berhasil",Toast.LENGTH_LONG).show();
                        Log.d("data user",userData.toString());
                        setPreference(userData);
                        if (userData.getGroup_user().equals(1))
                            move.moveActivity(mContext, SplashActivity.class);
                        else
                            move.moveActivity(mContext, SplashActivity.class);
                        finish();
                    }else{
                        Toasty.error(mContext,"Username dan password salah",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toasty.error(mContext,"Username dan password salah",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                pDialog.hide();
//                new ProgressDialog(mContext)
//                        .setTitle("Oops...")
//                        .d("Koneksi bermasalah!")
//                        .show();
//                pDialog = new ProgressDialog(ActivityLogin.this);
//                //  pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                pDialog.setMessage("Tidak ada koneksi");
//                pDialog.show();
                Toasty.success(mContext,"Koneksi Tidak ada",Toast.LENGTH_LONG).show();

                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
        });
    }

    private void setPreference(DataUser du){
        Prefs.putInt(SPref.getIdUser(),du.getId_user());
        Prefs.putString(SPref.getUSERNAME(),du.getUsername());
        Prefs.putString(SPref.getNAME(),du.getName());
        Prefs.putString(SPref.getEMAIL(),du.getEmail());
        Prefs.putString(SPref.getNoTelp(),du.getNo_telp());
        Prefs.putString(SPref.getJenisKelamin(),du.getJenis_kelamin().toString());
        Prefs.putString(SPref.getPHOTO(),du.getPhoto());
        Prefs.putString(SPref.getLastUpdate(),du.getLast_update().toString());
        Prefs.putString(SPref.getALAMAT(),du.getAlamat());
        Prefs.putInt(SPref.getGroupUser(),du.getGroup_user());
        Prefs.putString(SPref.getPASSWORD(),du.getPassword().toString());
    }
}
