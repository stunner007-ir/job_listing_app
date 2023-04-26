package com.stunner007.ListJobs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class JobDetailActivity extends AppCompatActivity {

    JobModel jobModel;
    TextView textViewDepartment, textViewSalary, textViewDescription, textViewName;
    Button buttonBackToListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get data from parent activity(joblistactivity/savedjoblistactivity)
        jobModel = getIntent().getParcelableExtra("jobModel");

        textViewName = findViewById(R.id.textViewName);
        textViewSalary = findViewById(R.id.textViewSalary);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDepartment = findViewById(R.id.textViewDepartment);
        buttonBackToListing = findViewById(R.id.buttonBackToListing);

        //if job data was not null then we can bind data with textviews
        if (jobModel != null) {
            textViewDepartment.setText(jobModel.getDept());
            textViewDescription.setText(jobModel.getDesc());
            textViewSalary.setText(jobModel.getSalary() + "");
            textViewName.setText(jobModel.getTitle());
        }

        buttonBackToListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //this method used when you click on top left back arrow
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}