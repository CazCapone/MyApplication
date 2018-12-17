package com.example.jduclos1.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class mainActivity extends AppCompatActivity {

    //sql stuff
    ItemAdapterSQL itemAdapterSQL;
    Context thisContext;
    ListView myListView;
    TextView progressTextView;
    Map<String, Double> exMap = new LinkedHashMap<String, Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button calendarActBtn = (Button) findViewById(R.id.calBtn);
        Button listsActBtn = (Button) findViewById(R.id.listBtn);
        Button groupsActBtn = (Button)findViewById(R.id.groupMgtBtn);
        Button mySQLActBtn = (Button)findViewById(R.id.mySQLBtn);



        //Opens activity within app
        groupsActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupMgtIntent = new Intent(getApplicationContext(), GroupMgtActivity.class);
                startActivity(groupMgtIntent);
            }
        });

        listsActBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(listIntent);
            }
        });

        mySQLActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sqlIntent = new Intent(getApplicationContext(), MySQLActivity.class);
                startActivity(sqlIntent);
            }
        });



        //Opens activity outside of app
        Button googleBtn = (Button) findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String google = "http://www.google.com";
                Uri webAddress = Uri.parse(google);

                Intent gotoGoogle = new Intent(Intent.ACTION_VIEW, webAddress);
                if (gotoGoogle.resolveActivity(getPackageManager()) != null){
                    startActivity(gotoGoogle);
                }
            }
        });

        //sql stuff
        Resources res = getResources();
        myListView = (ListView)findViewById(R.id.myListView);
        progressTextView = (TextView) findViewById(R.id.progressTextView);
        thisContext = this;

        progressTextView.setText("");
        Button dataBtn = (Button)findViewById(R.id.dataBtn);
        dataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData getData = new GetData();
                getData.execute("");
            }
        });
    }

    private class GetData extends AsyncTask<String, String, String>{

        String msg = "";
        //jdbc driver name and dbase url
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        static final String DB_URL = "jdbc:mysql://" + DBConnector.DATABASE_URL + "/" + DBConnector.DATABASE_NAME;

        protected void onPreExecute(){
            progressTextView.setText("Connecting to database...");
        }

        @Override
        protected String doInBackground(String... strings) {

            Connection conn = null;
            Statement state = null;

            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, DBConnector.USERNAME, DBConnector.PASSWORD);

                state = conn.createStatement();
                String sql = "SELECT * FROM table";
                ResultSet results = state.executeQuery(sql);

                while (results.next()){
                    String name = results.getString("name");
                    double price = results.getDouble("price");

                    exMap.put(name, price);
                }

                msg = "Done";

                results.close();
                state.close();
                conn.close();


            } catch (SQLDataException connError){
                msg = "Error!";
                connError.printStackTrace();;
            }catch (ClassNotFoundException e){
                msg = "diff error";
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (state != null){
                        state.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (conn != null){
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String msg){
            progressTextView.setText(this.msg);

            if (exMap.size() > 0 ){
                itemAdapterSQL = new ItemAdapterSQL(thisContext, exMap);
                myListView.setAdapter(itemAdapterSQL);
            }
        }
    }
}
