package com.example.mzstudio.profilsekolah;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /*@BindView(R.id.tv_namauser) TextView _lbnama;
    @BindView(R.id.tv_email) TextView _lbemail;
    @BindView(R.id.image_user) CircularImageView img_user;*/

    TextView tv_nama,tv_email;
    CircularImageView img_user;

    koneksi k = new koneksi();

    public String id_user,nama_user,email_user,hak_akses;
    private byte[] foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        Intent intent = getIntent();
        id_user = intent.getStringExtra("iduser");

        k.sql="select *from data_user where id_user='"+id_user+"'";
        k.ambil();
        try {
            if(k.rs.next()){
                nama_user = k.rs.getString("nama_user");
                email_user = k.rs.getString("email");
                hak_akses = k.rs.getString("hak_akses");
                foto = k.rs.getBytes("foto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View vi = navigationView.getHeaderView(0);
        tv_nama = (TextView)vi.findViewById(R.id.tv_namauser);
        tv_email = (TextView)vi.findViewById(R.id.tv_email);
        img_user = (CircularImageView)vi.findViewById(R.id.image_user);

        tv_nama.setText(nama_user);
        tv_email.setText(email_user);
        /*img_user.setImageBitmap(Bitmap.createScaledBitmap(bmp, img_user.getWidth(),
                img_user.getHeight(), false));*/
        Bitmap bmp = BitmapFactory.decodeByteArray(foto, 0, foto.length);
        img_user.setImageBitmap(bmp);



        //biar ga balik lagi ke home
        if(null == savedInstanceState) {
            //set Main Content
            FHome fHome = new FHome();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContent,fHome).commit();

            toolbar.setTitle(R.string.menu_Home);
            navigationView.setCheckedItem(R.id.nav_home);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
            FHome fHome = new FHome();
            fragmentTransaction.replace(R.id.flContent,fHome).commit();
            toolbar.setTitle(R.string.menu_Home);

        } else if (id == R.id.nav_daerah) {
            FDaerah fDaerah = new FDaerah();
            fragmentTransaction.replace(R.id.flContent,fDaerah).commit();
            toolbar.setTitle(R.string.menu_daerah);


        } else if (id == R.id.nav_sekolah) {
            FSekolah fSekolah = new FSekolah();
            fragmentTransaction.replace(R.id.flContent,fSekolah).commit();
            toolbar.setTitle(R.string.menu_sekolah);

        } else if (id == R.id.nav_guru) {
            FGuru fGuru = new FGuru();
            fragmentTransaction.replace(R.id.flContent,fGuru).commit();
            toolbar.setTitle(R.string.menu_guru);

        } else if (id == R.id.nav_siswa) {
            FSiswa fSiswa = new FSiswa();
            fragmentTransaction.replace(R.id.flContent,fSiswa).commit();
            toolbar.setTitle(R.string.menu_siswa);

        } else if (id == R.id.nav_pencarian) {
            FragmentPencarian fcari = new FragmentPencarian();
            fragmentTransaction.replace(R.id.flContent,fcari).commit();
            toolbar.setTitle(R.string.menu_cari);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
