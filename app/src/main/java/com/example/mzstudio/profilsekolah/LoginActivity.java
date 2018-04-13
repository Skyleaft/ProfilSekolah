package com.example.mzstudio.profilsekolah;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mzstudio.profilsekolah.Koneksi.koneksi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.txt_uname) EditText _unameText;
    @BindView(R.id.txt_pass) EditText _passwordText;

    koneksi k = new koneksi();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hak,iduser;

                ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setTitle("Memproses...");
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Harap Tunggu...");
                _loginButton.setEnabled(false);
                progressDialog.show();


                if(_unameText.getText().toString().equals("") || _passwordText.getText().toString().equals("")){
                    _loginButton.setEnabled(true);
                    progressDialog.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("Pemberitahuan");
                    alertDialog.setMessage("Nama Pengguna atau Kata Sandi masih Kosong");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{


                    k.konek();
                    if(k.con==null){
                        if(_unameText.getText().toString().equals("admin") && _passwordText.getText().toString().equals("admin")){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(),"Gagal koneksi ke server",Toast.LENGTH_SHORT).show();

                        }
                        _loginButton.setEnabled(true);
                        progressDialog.dismiss();
                    }
                    else{
                        k.sql="select * from akun where username = '"+_unameText.getText().toString()+"' and password = '"+_passwordText.getText().toString()+"'";
                        k.ambil();
                        try {
                            if(k.rs.next()){
                                hak = k.rs.getString("hak_akses");
                                iduser = k.rs.getString("id_user");
                                if(hak.equals("Admin")){
                                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    Intent i=new Intent(getApplicationContext(), MainActivity.class);
                                    i.putExtra("iduser",iduser);
                                    startActivity(i);
                                    _loginButton.setEnabled(true);
                                    progressDialog.dismiss();
                                }
                                else{
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    _loginButton.setEnabled(true);
                                    progressDialog.dismiss();
                                }

                            }
                            else{
                                _loginButton.setEnabled(true);
                                progressDialog.dismiss();
                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                alertDialog.setTitle("Pemberitahuan");
                                alertDialog.setMessage("Nama Pengguna atau Kata Sandi Salah");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

    }
    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
