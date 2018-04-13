package com.example.mzstudio.profilsekolah;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.codecrafters.tableview.TableView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabDSekolah extends Fragment {

    @BindView(R.id.btn_pilih) Button bt_pilih;
    @BindView(R.id.btn_baru) FloatingActionButton bt_baru;
    @BindView(R.id.btn_simpan) FloatingActionButton bt_simpan;
    @BindView(R.id.btn_hapus) FloatingActionButton bt_hapus;
    @BindView(R.id.btn_ubah) FloatingActionButton bt_ubah;
    @BindView(R.id.fab_menu)FloatingActionMenu fabmenu;
    @BindView(R.id.txt_npsn) EditText _txtnpsn;
    @BindView(R.id.txt_namasklh) EditText _txtnamasklh;
    @BindView(R.id.txt_alamat) EditText _txtalamat;
    @BindView(R.id.txt_rt) EditText _txtrt;
    @BindView(R.id.txt_rw) EditText _txtrw;
    @BindView(R.id.txt_notelp) EditText _txttelp;
    @BindView(R.id.txt_fax) EditText _txtfax;
    @BindView(R.id.txt_email) EditText _txtemail;
    @BindView(R.id.txt_web) EditText _txtweb;
    @BindView(R.id.txt_kepsek) EditText _txtkepsek;
    @BindView(R.id.sp_prov) Spinner spprov;
    @BindView(R.id.sp_kab) Spinner spkab;
    @BindView(R.id.sp_kec) Spinner spkec;
    @BindView(R.id.sp_desa) Spinner spdes;
    @BindView(R.id.btn_pick) FloatingActionButton bt_pick;

    koneksi k = new koneksi();

    CircularImageView img_sekolah;
    private List<String> dataprov = new ArrayList<>();
    private List<String> datakab = new ArrayList<>();
    private List<String> datakec = new ArrayList<>();
    private List<String> datades = new ArrayList<>();
    private String kodeprov,kodekab,kodekec,kodedes,id_sekolah;

    private boolean terpilih =false;

    private Uri selectedImageUri;

    String _getprov,_getkab,_getkec,_getdes;

    public TabDSekolah() {
        // Required empty public constructor
    }

    Integer SELECT_FILE=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_dsekolah, container, false);
        final CoordinatorLayout rootlayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorlayout);
        ButterKnife.bind(this, v);
        img_sekolah = (CircularImageView)v.findViewById(R.id.image_sekolah);


        loadSpinnerData();
        spprov.setSelection(-1);
        spprov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String _kdprov = spprov.getSelectedItem().toString();
                String[] separated = _kdprov.split("-");
                kodeprov = separated[0];

                datakab.clear();
                String c,d;
                k.sql="select * from kabupaten where id_provinsi='"+kodeprov+"'";
                k.ambil();
                try {
                    while(k.rs.next()){
                        c = k.rs.getString("id_kabupaten");
                        d = k.rs.getString("nama_kabupaten");
                        datakab.add(c+" - "+d);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datakab);
                adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spkab.setAdapter(adapter2);

                if(terpilih == true){
                    setSpinText(spkab,_getkab);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spkab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String _kdkab = spkab.getSelectedItem().toString();
                String[] separated2 = _kdkab.split("-");
                kodekab = separated2[0];

                datakec.clear();
                String c,d;
                k.sql="select * from kecamatan where id_kabupaten='"+kodekab+"'";
                k.ambil();
                try {
                    while(k.rs.next()){
                        c = k.rs.getString("id_kecamatan");
                        d = k.rs.getString("nama_kecamatan");
                        datakec.add(c+" - "+d);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datakec);
                adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spkec.setAdapter(adapter2);

                if(terpilih == true){
                    setSpinText(spkec,_getkec);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spkec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String _kdkec = spkec.getSelectedItem().toString();
                String[] separated2 = _kdkec.split("-");
                kodekec = separated2[0];

                datades.clear();
                String c,d;
                k.sql="select * from desa where id_kecamatan='"+kodekec+"'";
                k.ambil();
                try {
                    while(k.rs.next()){
                        c = k.rs.getString("id_desa");
                        d = k.rs.getString("nama_desa");
                        datades.add(c+" - "+d);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datades);
                adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spdes.setAdapter(adapter2);

                if(terpilih == true){
                    setSpinText(spdes,_getdes);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spdes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String _kddes = spdes.getSelectedItem().toString();
                String[] separated2 = _kddes.split("-");
                kodedes = separated2[0];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        bt_pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ListSekolah.class);
                startActivityForResult(i, 1);
            }
        });

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);

                String npsn = _txtnpsn.getText().toString();
                String nama = _txtnamasklh.getText().toString();
                String alamat = _txtalamat.getText().toString();
                String rw = _txtrw.getText().toString();
                String rt = _txtrt.getText().toString();
                String notelp = _txttelp.getText().toString();
                String email = _txtemail.getText().toString();
                String fax = _txtfax.getText().toString();
                String web = _txtweb.getText().toString();
                String kepsek = _txtkepsek.getText().toString();

                if(npsn.equals("") || nama.equals("")){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Data belum lengkap!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    try{
                        k.sql ="select *from sekolah where id_sekolah='"+npsn+"' and nama_sekolah='"+nama+"'";
                        k.ambil();
                        if(k.rs.next()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Pemberitahuan");
                            alertDialog.setMessage("Sekolah Sudah Ada!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else{
                            byte[] imagePath;
                            if(selectedImageUri ==null){
                                Bitmap def = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tutwuri);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                def.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                imagePath = stream.toByteArray();
                            }
                            else {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                imagePath = stream.toByteArray();
                            }

                            k.sql="insert into sekolah values(?,?,?,?,?,?,?,?,?,?,?,?)";
                            k.CUD();
                            k.ps.setString(1,npsn);
                            k.ps.setString(2,nama);
                            k.ps.setString(3,alamat);
                            k.ps.setString(4,kodedes);
                            k.ps.setString(5,rw);
                            k.ps.setString(6,rt);
                            k.ps.setString(7,notelp);
                            k.ps.setString(8,fax);
                            k.ps.setBytes(9,imagePath);
                            k.ps.setString(10,email);
                            k.ps.setString(11,web);
                            k.ps.setString(12,kepsek);
                            k.ps.executeUpdate();

                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            Snackbar.make(rootlayout, "Data berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                            bersih();
                        }
                    }catch (SQLException e){
                        Log.d("Tag", "err : "+e);
                    } catch (FileNotFoundException e) {
                        Log.d("Tag", "err : "+e);
                    } catch (IOException e) {
                        Log.d("Tag", "err : "+e);
                    }


                }
            }
        });

        bt_baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                bersih();
            }
        });

        bt_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabmenu.close(true);
                if(id_sekolah==null){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Data Belum Dipilih!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Peringatan!");
                    builder.setMessage("Yakin Ubah data Sekolah : "+id_sekolah);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                    try {
                        byte[] imagePath;
                        if(selectedImageUri ==null){
                            BitmapDrawable drawable = (BitmapDrawable) img_sekolah.getDrawable();
                            Bitmap def = drawable.getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            def.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            imagePath = stream.toByteArray();
                        }
                        else {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            imagePath = stream.toByteArray();
                        }

                        String npsn = _txtnpsn.getText().toString();
                        String nama = _txtnamasklh.getText().toString();
                        String alamat = _txtalamat.getText().toString();
                        String rw = _txtrw.getText().toString();
                        String rt = _txtrt.getText().toString();
                        String notelp = _txttelp.getText().toString();
                        String email = _txtemail.getText().toString();
                        String fax = _txtfax.getText().toString();
                        String web = _txtweb.getText().toString();
                        String kepsek = _txtkepsek.getText().toString();

                        k.sql="update sekolah set id_sekolah=?,nama_sekolah=?,alamat_sekolah=?,id_desa=?,rw=?,rt=?,no_telp=?,no_fax=?,image=?,email=?,website=?,kepala_sekolah=? where id_sekolah='"+id_sekolah+"'";
                        k.CUD();
                        k.ps.setString(1,npsn);
                        k.ps.setString(2,nama);
                        k.ps.setString(3,alamat);
                        k.ps.setString(4,kodedes);
                        k.ps.setString(5,rw);
                        k.ps.setString(6,rt);
                        k.ps.setString(7,notelp);
                        k.ps.setString(8,fax);
                        k.ps.setBytes(9,imagePath);
                        k.ps.setString(10,email);
                        k.ps.setString(11,web);
                        k.ps.setString(12,kepsek);
                        k.ps.executeUpdate();
                        // Do nothing, but close the dialog
                        dialog.dismiss();
                        bersih();
                        Snackbar.make(rootlayout, "Data berhasil Diubah", Snackbar.LENGTH_LONG).show();

                    }catch (SQLException e) {

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });

        bt_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                if(id_sekolah==null){
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Data Belum Dipilih!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Peringatan!");
                    builder.setMessage("Yakin Hapus data Sekolah : "+id_sekolah);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            k.sql="delete from sekolah where id_sekolah='"+id_sekolah+"'";
                            k.crud();
                            // Do nothing, but close the dialog
                            dialog.dismiss();
                            bersih();
                            Snackbar.make(rootlayout, "Data berhasil Dihapus", Snackbar.LENGTH_LONG).show();

                        }
                    });
                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1 : {
                if (resultCode == Activity.RESULT_OK) {
                    id_sekolah = data.getStringExtra("id_sekolah");
                    terpilih=true;
                    byte[] foto=null;
                    k.sql="select *from sekolah where id_sekolah='"+id_sekolah+"'";
                    k.ambil();
                    try {
                        if(k.rs.next()){
                            _txtnpsn.setText(k.rs.getString("id_sekolah"));
                            _txtnamasklh.setText(k.rs.getString("nama_sekolah"));
                            _txtalamat.setText(k.rs.getString("alamat_sekolah"));
                            kodedes = k.rs.getString("id_desa");
                            _txtrt.setText(k.rs.getString("rt"));
                            _txtrw.setText(k.rs.getString("rw"));
                            _txttelp.setText(k.rs.getString("no_telp"));
                            _txtfax.setText(k.rs.getString("no_fax"));
                            foto = k.rs.getBytes("image");
                            _txtemail.setText(k.rs.getString("email"));
                            _txtweb.setText(k.rs.getString("website"));
                            _txtkepsek.setText(k.rs.getString("kepala_sekolah"));

                        }
                        Bitmap bmp = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                        img_sekolah.setImageBitmap(bmp);

                        _getdes = kodedes;
                        k.sql = "select *from desa where id_desa='"+kodedes+"'";
                        k.ambil();
                        if(k.rs.next()){
                            _getdes = _getdes +"- " + k.rs.getString("nama_desa");
                            kodekec = k.rs.getString("id_kecamatan");
                        }

                        _getkec = kodekec;
                        k.sql = "select *from kecamatan where id_kecamatan='"+kodekec+"'";
                        k.ambil();
                        if(k.rs.next()){
                            _getkec = _getkec +"- " + k.rs.getString("nama_kecamatan");
                            kodekab = k.rs.getString("id_kabupaten");
                        }

                        _getkab = kodekab;
                        k.sql = "select *from kabupaten where id_kabupaten='"+kodekab+"'";
                        k.ambil();
                        if(k.rs.next()){
                            _getkab = _getkab +"- " + k.rs.getString("nama_kabupaten");
                            kodeprov = k.rs.getString("id_provinsi");
                        }

                        _getprov = kodeprov;
                        k.sql = "select *from provinsi where id_provinsi='"+kodeprov+"'";
                        k.ambil();
                        if(k.rs.next()){
                            _getprov = _getprov +"- " + k.rs.getString("nama_provinsi");
                        }



                        setSpinText(spdes,_getdes);
                        setSpinText(spdes,_getkec);
                        setSpinText(spkab,_getkab);
                        setSpinText(spprov,_getprov);


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                break;
            }
        }
        if(resultCode== Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                selectedImageUri = data.getData();
                img_sekolah.setImageURI(selectedImageUri);
            }
        }
    }


    private void bersih(){
        spprov.setSelection(0);
        spkab.setSelection(-1);
        spkec.setSelection(-1);
        _txtalamat.setText("");
        _txtemail.setText("");
        _txttelp.setText("");
        _txtfax.setText("");
        _txtkepsek.setText("");
        _txtnamasklh.setText("");
        _txtnpsn.setText("");
        _txtrt.setText("");
        _txtrw.setText("");
        _txtweb.setText("");
        selectedImageUri=null;
        img_sekolah.setImageResource(R.drawable.tutwuri);
        terpilih = false;
        kodedes=null;
    }

    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }

    private void loadSpinnerData(){

        try{
            String a,b;
            k.sql="select * from provinsi";
            k.ambil();
            while(k.rs.next()){
                a = k.rs.getString("id_provinsi");
                b = k.rs.getString("nama_provinsi");
                dataprov.add(a+" - "+b);

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, dataprov);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spprov.setAdapter(adapter);


        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private void SelectImage(){

        final CharSequence[] items={"Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ambil Foto");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

}
