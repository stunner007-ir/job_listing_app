package com.stunner007.ListJobs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

//used to show saved and unsaved record on listview
public class JobAdapter extends ArrayAdapter<JobModel> {

    Context mCtx;
    int listLayoutRes;
    List<JobModel> jobList;
    SQLiteDatabase mDatabase;
    private String saved = "0";
    private String message = "";
    private boolean isSaved;
    private Cursor cursorjobs;

    //paramater construction
    public JobAdapter(Context mCtx, int listLayoutRes, List<JobModel> jobList, SQLiteDatabase mDatabase, boolean isSaved) {
        super(mCtx, listLayoutRes, jobList);
        this.mCtx = mCtx;
        this.isSaved = isSaved;
        this.listLayoutRes = listLayoutRes;
        this.jobList = jobList;
        this.mDatabase = mDatabase;
    }

    //load data on view to show every record
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        final JobModel jobModel = jobList.get(position);


        TextView textViewJobTitle = view.findViewById(R.id.textViewJobTitle);
        TextView textViewJobDescription = view.findViewById(R.id.textViewJobDescription);

        LinearLayout llItem = view.findViewById(R.id.llItem);

        textViewJobTitle.setText(jobModel.getTitle());
        textViewJobDescription.setText(jobModel.getDesc());


        ImageView imgSave = view.findViewById(R.id.imgSave);
        ImageView imgDelete = view.findViewById(R.id.imgDelete);

        if (jobModel.getSaved() == 0) {
            imgSave.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.unsaved));
        } else {
            imgSave.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.saved));
        }

        //adding a clicklistener to button
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateJob(jobModel);
            }
        });

        //adding a clicklistener to button
        llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx,JobDetailActivity.class);
                intent.putExtra("jobModel",jobModel);
                mCtx.startActivity(intent);
            }
        });

        //the delete operation
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM jobs WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{jobModel.getId()});
                        reloadJobsFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    //update record for save unsaved
    private void updateJob(final JobModel jobModel) {
        if (jobModel.getSaved() == 0) {
            saved = "1";
            message = "Job Saved";
        } else {
            saved = "0";
            message = "Job UnSaved";
        }

        String sql = "UPDATE jobs \n" +
                "SET name = ?, \n" +
                "department = ?, \n" +
                "description = ?, \n" +
                "createddate = ?, \n" +
                "salary = ?, \n" +
                "saved = ? \n" +
                "WHERE id = ?;\n";

        try {
            mDatabase.execSQL(sql, new String[]{jobModel.getTitle(), jobModel.getDept(), jobModel.getDesc(), jobModel.getCreatedDate(), String.valueOf(jobModel.getSalary()), saved, String.valueOf(jobModel.getId())});
        } catch (SQLiteException e) {
            Log.e("Exception", e.getMessage());
        }
        Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
        reloadJobsFromDatabase();

    }

    //reload data again for update ui
    private void reloadJobsFromDatabase() {
        if(isSaved) {
            cursorjobs = mDatabase.rawQuery("SELECT * FROM jobs where saved=1", null);
        }else {
            cursorjobs = mDatabase.rawQuery("SELECT * FROM jobs", null);
        }
        if (cursorjobs.moveToFirst()) {
            jobList.clear();
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
        cursorjobs.close();
        notifyDataSetChanged();
    }


}
