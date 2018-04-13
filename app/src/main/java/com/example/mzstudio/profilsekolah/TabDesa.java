package com.example.mzstudio.profilsekolah;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabDesa extends Fragment {

    @BindView(R.id.btn_baru) FloatingActionButton bt_baru;
    @BindView(R.id.btn_simpan) FloatingActionButton bt_simpan;
    @BindView(R.id.btn_hapus) FloatingActionButton bt_hapus;
    @BindView(R.id.btn_ubah) FloatingActionButton bt_ubah;
    @BindView(R.id.fab_menu)FloatingActionMenu fabmenu;
    @BindView(R.id.txt_kodedesa) EditText _txtkddes;
    @BindView(R.id.txt_namadesa) EditText _txtnamades;
    @BindView(R.id.table_des) TableView<String[]> tbdes;
    @BindView(R.id.sp_prov) Spinner spprov;
    @BindView(R.id.sp_kab) Spinner spkab;
    @BindView(R.id.sp_kec) Spinner spkec;

    private List<String> dataprov = new ArrayList<>();
    private List<String> datakab = new ArrayList<>();
    private List<String> datakec = new ArrayList<>();
    private String kodeprov,kodekab,kodekec,kodedes;

    koneksi k = new koneksi();

    String _getkab,_getkec;

    private boolean terpilih =false;

    public TabDesa() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_desa, container, false);
        final CoordinatorLayout rootlayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorlayout);
        ButterKnife.bind(this, v);
        loadSpinnerData();
        tampildata();



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

                if(terpilih == false){
                    _txtkddes.setText("");
                    _txtkddes.append(kodekec);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                String namades = _txtnamades.getText().toString();
                String kddes = _txtkddes.getText().toString();

                if(namades.equals("") || kddes.equals("") || kddes.length()<9){
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
                        k.sql ="select *from desa where id_desa='"+kddes+"' or nama_desa='"+namades+"'";
                        k.ambil();
                        if(k.rs.next()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Pemberitahuan");
                            alertDialog.setMessage("Data Sudah Ada!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else{
                            k.sql="insert into desa values('"+kddes+"','"+namades+"','"+kodekec+"')";
                            k.crud();
                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            Snackbar.make(rootlayout, "Data berhasil Disimpan", Snackbar.LENGTH_LONG).show();
                            bersih();
                            tampildata();
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
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


        tbdes.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                terpilih = true;
                kodedes = ((String[])clickedData)[0];
                _txtkddes.setText(kodedes);
                _txtnamades.setText(((String[])clickedData)[1]);
                kodekec = ((String[])clickedData)[2];
                String getkec = kodekec;
                String getkab=null;
                String getprov=null;

                try{
                    k.sql = "select *from kecamatan where id_kecamatan='"+kodekec+"'";
                    k.ambil();
                    if(k.rs.next()){
                        getkec = getkec +"- " + k.rs.getString("nama_kecamatan");
                        _getkec = getkec;
                        getkab = k.rs.getString("id_kabupaten");
                    }

                    k.sql = "select *from kabupaten where id_kabupaten='"+getkab+"'";
                    k.ambil();
                    if(k.rs.next()){
                        getkab = getkab +"- " + k.rs.getString("nama_kabupaten");
                        _getkab = getkab;
                        getprov = k.rs.getString("id_provinsi");
                    }

                    k.sql = "select *from provinsi where id_provinsi='"+getprov+"'";
                    k.ambil();
                    if(k.rs.next()){
                        getprov = getprov +"- " + k.rs.getString("nama_provinsi");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                setSpinText(spprov,getprov);
                setSpinText(spkab,getkab);
                setSpinText(spkec,getkec);


                //Toast.makeText(getContext(), "Data yang Dipilih : "+((String[])clickedData)[1], Toast.LENGTH_SHORT).show();
            }
        });

        bt_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabmenu.close(true);
                if(kodedes==null){
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
                    builder.setMessage("Yakin Ubah data Desa : "+kodedes);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            k.sql="update desa set id_desa='"+_txtkddes.getText().toString()+"',nama_desa='"+_txtnamades.getText().toString()+"',id_kecamatan='"+kodekec+"' where id_desa='"+kodedes+"'";
                            k.crud();
                            // Do nothing, but close the dialog
                            dialog.dismiss();
                            bersih();
                            tampildata();
                            Snackbar.make(rootlayout, "Data berhasil Diubah", Snackbar.LENGTH_LONG).show();


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
                if(kodedes==null){
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
                    builder.setMessage("Yakin Hapus data Desa: "+kodedes);
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            k.sql="delete from desa where id_desa='"+kodedes+"'";
                            k.crud();
                            // Do nothing, but close the dialog
                            dialog.dismiss();
                            bersih();
                            tampildata();
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


    private void bersih(){
        spprov.setSelection(0);
        spkab.setSelection(-1);
        spkec.setSelection(-1);
        _txtkddes.setText("");
        _txtnamades.setText("");
        kodedes=null;
        terpilih=false;
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

    private String[] desheader={"Kode","Nama Desa/Kelurahan"};
    private List<String[]> datades = new ArrayList<>();

    private void tampildata() {
        datades.clear();
        try {
            String a, b, c;
            k.sql = "select * from desa";
            k.ambil();
            while (k.rs.next()) {
                a = k.rs.getString("id_desa");
                b = k.rs.getString("nama_desa");
                c = k.rs.getString("id_kecamatan");
                datades.add(new String[]{a, b,c});

            }

            tbdes.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), desheader));
            tbdes.setDataAdapter(new SimpleTableDataAdapter(getContext(), datades));

            TableColumnDpWidthModel tableColumnModel = new TableColumnDpWidthModel( getContext(), 2 );
            tableColumnModel.setColumnWidth( 0, 160 );
            tableColumnModel.setColumnWidth( 1, 300 );
            tbdes.setColumnModel(tableColumnModel);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
