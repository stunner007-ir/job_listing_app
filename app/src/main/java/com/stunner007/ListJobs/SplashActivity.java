package com.stunner007.ListJobs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.stunner007.ListJobs.databinding.ActivitySplashBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "myjobdatabase";
    String saved = "0";
    SQLiteDatabase mDatabase;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_splash);

        //handler object used to delayed 3 sec and open listing screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 3000);

        //pref to store value
        prefs = getSharedPreferences("com.singapore.career", MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
            //creating a database
            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            createEmployeeTable();

            addJob("job title1", "job description1", "10000", "Human Resource");
            addJob("job title2", "job description2", "20000", "Technical");
        }
    }

    //In this method we will do the create operation
    private void addJob(String name, String description, String salary, String dept) {

        //getting the current time for creating date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String createddate = sdf.format(cal.getTime());


        String insertSQL = "INSERT INTO jobs \n" +
                "(name, department, description, createddate, salary,saved)\n" +
                "VALUES \n" +
                "(?, ?, ?, ?, ?,?);";

        //using the same method execsql for inserting values
        //this time it has two parameters
        //first is the sql string and second is the parameters that is to be binded with the query
        try {
            mDatabase.execSQL(insertSQL, new String[]{name, dept, description, createddate, salary, saved});
        } catch (SQLiteException e) {
            Log.e("Exception", e.getMessage());
        }

    }

    //this method will create the table
    //as we are going to call this method everytime we will launch the application
    //I have added IF NOT EXISTS to the SQL
    //so it will only create the table when the table is not already created
    private void createEmployeeTable() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS jobs (\n" +
                        "    id INTEGER NOT NULL CONSTRAINT jobs_pk PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name varchar(200) NOT NULL,\n" +
                        "    department varchar(200) NOT NULL,\n" +
                        "    description varchar(200) NOT NULL,\n" +
                        "    createddate datetime NOT NULL,\n" +
                        "    salary double NOT NULL,\n" +
                        "    saved INTEGER NOT NULL\n" +
                        ");"
        );
    }
}