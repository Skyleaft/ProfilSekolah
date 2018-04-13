package com.example.mzstudio.profilsekolah;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FGuru extends Fragment {

    @BindView(R.id.btn_pilih) Button bt_pilih;
    @BindView(R.id.btn_pilihtgl) Button bt_pilihtgl;
    @BindView(R.id.btn_baru) FloatingActionButton bt_baru;
    @BindView(R.id.btn_simpan) FloatingActionButton bt_simpan;
    @BindView(R.id.btn_hapus) FloatingActionButton bt_hapus;
    @BindView(R.id.btn_ubah) FloatingActionButton bt_ubah;
    @BindView(R.id.fab_menu)FloatingActionMenu fabmenu;
    @BindView(R.id.tv_namasekolah) TextView _namasklh;
    @BindView(R.id.txt_nip) EditText _txtnip;
    @BindView(R.id.txt_nama) EditText _txtnama;
    @BindView(R.id.radioGender) RadioGroup _gender;
    @BindView(R.id.rb_laki) RadioButton rblaki;
    @BindView(R.id.rb_perempuan) RadioButton rbperempuan;
    @BindView(R.id.txt_tmplahir) EditText _txttempatlhr;
    @BindView(R.id.txt_tgl) EditText tgl_lahir;
    @BindView(R.id.txt_pendidikan) EditText _txtpendidikan;
    @BindView(R.id.txt_jurusan) EditText _txtjurusan;
    @BindView(R.id.txt_alamat) EditText _txtalamat;
    @BindView(R.id.txt_notelp) EditText _txtnotelp;
    @BindView(R.id.txt_email) EditText _txtemail;

    public FGuru() {
        // Required empty public constructor
    }

    private String get_tgllahir,id_sekolah,nama_sekolah;




    koneksi k = new koneksi();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guru, container, false);
        final CoordinatorLayout rootlayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorlayout);
        ButterKnife.bind(this, v);


        bt_pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ListSekolah.class);
                startActivityForResult(i, 1);
            }
        });

        bt_pilihtgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        get_tgllahir = simpleDateFormat.format(newDate.getTime());
                        tgl_lahir.setText(get_tgllahir);
                    }

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);

                String nip = _txtnip.getText().toString();
                String nama = _txtnama.getText().toString();
                String tempatlahir = _txttempatlhr.getText().toString();
                String tanggal_lahir = get_tgllahir;
                String pendidikan = _txtpendidikan.getText().toString();
                String jurusan = _txtjurusan.getText().toString();
                String alamat = _txtalamat.getText().toString();
                String no_telp = _txtnotelp.getText().toString();
                String email = _txtemail.getText().toString();

                String gender;
                if(rblaki.isChecked()==true){
                    gender="Laki - laki";
                }else{
                    gender="Perempuan";
                }


                if (nip.equals("") || nama.equals("") || id_sekolah == null) {
                    android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Data belum lengkap!");
                    alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    try {
                        k.sql = "select *from guru where nip='" + nip + "' and nama_guru='" + nama + "'";
                        k.ambil();
                        if (k.rs.next()) {
                            android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Pemberitahuan");
                            alertDialog.setMessage("Data Sudah Ada!");
                            alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        } else {
                            k.sql = "insert into guru values('"+nip+"','"+nama+"','"+gender+"','"+tempatlahir+"','"+get_tgllahir+"','"+pendidikan+"','"+jurusan+"','"+alamat+"','"+no_telp+"','"+email+"','"+id_sekolah+"')";
                            k.crud();
                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            Snackbar.make(rootlayout, "Data berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                            bersih();
                        }
                    } catch (SQLException e) {
                        Log.d("Tag", "err : " + e);
                    }
                }
            }
        });


        return v;
    }

    private void bersih(){
        _txtemail.setText("");
        _txtnip.setText("");
        _txtnama.setText("");
        _txttempatlhr.setText("");
        tgl_lahir.setText("");
        _txtpendidikan.setText("");
        _txtjurusan.setText("");
        _txtalamat.setText("");
        _txtnotelp.setText("");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1 : {
                if (resultCode == Activity.RESULT_OK) {
                    id_sekolah = data.getStringExtra("id_sekolah");
                    nama_sekolah = data.getStringExtra("nama_sekolah");
                    _namasklh.setText(nama_sekolah);

                }
                break;
            }
        }
    }

}
