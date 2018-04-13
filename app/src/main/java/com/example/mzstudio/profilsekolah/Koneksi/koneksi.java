package com.example.mzstudio.profilsekolah.Koneksi;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Milzan Malik on 15/02/2018.
 */

public class koneksi {
    public  String ip="192.168.43.211:1433";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "DBProfilSekolah";
    String usr="milzan";
    String pass="123456";
    String ConnURL = null;
    public Statement st;
    public ResultSet rs;
    public String sql;
    public PreparedStatement ps;
    public Connection con;




    @SuppressLint("NewApi")
    public void konek() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";databaseName=" + db + ";";
            DriverManager.setLoginTimeout(5);
            con = DriverManager.getConnection(ConnURL,usr,pass);
            st=con.createStatement();
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }

    }




    public void ambil(){
        try {
            konek();
            rs=st.executeQuery(sql);
        } catch (SQLException e) {
            Log.e("Error ", e.getMessage());
        }
    }

    public void crud(){
        try {
            konek();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            Log.e("Error ", e.getMessage());
        }
    }

    public void CUD() throws SQLException{
        try{
            ps = con.prepareStatement(sql);
        } catch(SQLException e){
            Log.e("Error ", e.getMessage());
        }
    }
}
