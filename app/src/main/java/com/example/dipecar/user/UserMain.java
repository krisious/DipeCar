package com.example.dipecar.user;

import android.annotation.SuppressLint;


import android.content.Context;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dipecar.ActivityLogin;
import com.example.dipecar.R;
import com.example.dipecar.SPreferenced.SPref;
import com.example.dipecar.admin.AdminEditProfile;
import com.example.dipecar.api.client;
import com.example.dipecar.utils.move;
import com.pixplicity.easyprefs.library.Prefs;

import com.squareup.picasso.Picasso;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import es.dmoral.toasty.Toasty;

public class UserMain extends NavigationLiveo implements OnItemClickListener {
    Context mContext;
    private HelpLiveo mHelpLiveo;
//    AdminListUser adminListUser;

    @SuppressLint("ResourceAsColor")
    @Override
    public void onInt(Bundle savedInstanceState) {

        mContext=this;
        // User Information
//        adminListUser=new AdminListUser();
        this.userName.setText(Prefs.getString(SPref.getNAME(),""));
        this.userName.setTextColor(R.color.nliveo_black);
        this.userEmail.setText(Prefs.getString(SPref.getEMAIL(),""));
        this.userEmail.setTextColor(R.color.nliveo_black);

        this.userBackground.setImageResource(R.drawable.drawer_bg);
        Picasso.with(mContext)
                .load(client.getBaseUrlImage()+Prefs.getString(SPref.getPHOTO(),""))
                .resize(250, 250)
                .centerCrop()
                .into(this.userPhoto);

        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add("History Transaksi", R.drawable.ic_action_dock);
        mHelpLiveo.add(getString(R.string.mobil), R.drawable.ic_nav_transport);

        with(this).startingPosition(0)
                .addAllHelpItem(mHelpLiveo.getHelp())
                .selectorCheck(R.color.nliveo_purple_colorPrimaryDark)
                .colorItemDefault(R.color.white)
                .colorItemSelected(R.color.white)
                .backgroundList(R.color.nliveo_black)
                .colorItemIcon(R.color.colorAccent)
                .footerItem(getString(R.string.setting), R.drawable.ic_action_settings)
                .footerSecondItem(R.string.logout, R.drawable.ic_action_screen_locked_to_portrait)
                .footerNameColor(R.color.white)
                .footerIconColor(R.color.colorAccent)
                .footerSecondNameColor(R.color.white)
                .footerSecondIconColor(R.color.colorAccent)
                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickProfile)
                .setOnClickFooterSecond(onClickFooter)
                .build();

        int position = this.getCurrentPosition();
        this.setElevationToolBar(position != 2 ? 15 : 0);
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment=null;
        FragmentManager mFragmentManager = getSupportFragmentManager();

        if (position == 0) {
            mFragment = UserHistoryTransaksi.newInstance(mHelpLiveo.get(position).getName());
        } else if (position == 1) {
            mFragment = UserListCars.newInstance(mHelpLiveo.get(position).getName());
        } else if (position == 2) {//                mFragment = AdminListCart.newInstance(mHelpLiveo.get(position).getName());
        } else if (position == 3) {//                mFragment = adminListUser.newInstance(mHelpLiveo.get(position).getName(),"2");
        } else if (position == 4) {//                mFragment = adminListUser.newInstance(mHelpLiveo.get(position).getName(),"1");
        }
        if (mFragment != null){
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Prefs.clear();
            move.moveActivity(mContext, ActivityLogin.class);
        }
    };

    private View.OnClickListener onClickProfile= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
            move.moveActivity(mContext, AdminEditProfile.class);
        }
    };
}
