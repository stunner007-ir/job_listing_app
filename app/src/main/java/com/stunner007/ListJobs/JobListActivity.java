package com.stunner007.ListJobs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class JobListActivity extends AppCompatActivity {

    List<JobModel> jobList;
    SQLiteDatabase mDatabase;
    ListView listViewjobs;
    JobAdapter adapter;
    private boolean isSaved=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

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
        Cursor cursorjobs = mDatabase.rawQuery("SELECT * FROM jobs", null);

        if(jobList.size()>0){
            jobList.clear();
        }

        //if the cursor has some data
        if (cursorjobs.moveToFirst()) {

            do {

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
        adapter = new JobAdapter(this, R.layout.list_layout_job, jobList, mDatabase,isSaved);

        //adding the adapter to listview
        listViewjobs.setAdapter(adapter);
    }

    //bind the menu file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    //handle menu file item clicked(selected)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menuSaved){
            startActivity(new Intent(JobListActivity.this,SavedJobListActivity.class));
        }
       else if(item.getItemId()==R.id.logout){
             startActivity(new Intent(JobListActivity.this,MainActivity.class));

        }

        else if(item.getItemId()==R.id.addactivity){
            startActivity(new Intent(JobListActivity.this,AddJobActivity.class));

        }
        return true;
    }


    //open add job activity
    public void openAddJobActivity(View view) {
        startActivity(new Intent(this, AddJobActivity.class));
    }

}
