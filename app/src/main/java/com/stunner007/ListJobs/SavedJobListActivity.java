package com.stunner007.ListJobs;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavedJobListActivity extends AppCompatActivity {

    List<JobModel> jobList;
    SQLiteDatabase mDatabase;
    ListView listViewjobs;
    JobAdapter adapter;
    private boolean isSaved=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_job_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listViewjobs = (ListView) findViewById(R.id.listViewjobs);
        jobList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //opening the database
        mDatabase = openOrCreateDatabase(AddJobActivity.DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the jobs in the list
        showjobsFromDatabase();
    }

    private void showjobsFromDatabase() {
        //we used rawQuery(sql, selectionargs) for fetching all the jobs
        Cursor cursorjobs = mDatabase.rawQuery("SELECT * FROM jobs where saved=1", null);

        if(jobList.size()>0){
            jobList.clear();
        }

        //if the cursor has some data
        if (cursorjobs.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the job list
                jobList.add(new JobModel(
                        cursorjobs.getInt(0),
                        cursorjobs.getString(1),
                        cursorjobs.getString(2),
                        cursorjobs.getString(3),
                        cursorjobs.getString(4),
                        cursorjobs.getDouble(5),
                        cursorjobs.getInt(6)
                ));
            } while (cursorjobs.moveToNext());
        }
        //closing the cursor
        cursorjobs.close();

        //creating the adapter object
        adapter = new JobAdapter(this, R.layout.list_layout_job, jobList, mDatabase, isSaved);

        //adding the adapter to listview
        listViewjobs.setAdapter(adapter);
    }

    //this method used when you click on top left back arrow
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    
}