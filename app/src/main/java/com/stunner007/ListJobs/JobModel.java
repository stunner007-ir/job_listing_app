package com.stunner007.ListJobs;


import android.os.Parcel;
import android.os.Parcelable;

//capsule class or model class where jobs table column added with getter setter,
//implements Parcelable used to pass model object from one class to another class
public class JobModel implements Parcelable {
    int id,saved;
    String title, dept,desc, createdDate;
    double salary;

    //paramaterised constroctor
    public JobModel(int id, String title, String dept, String desc,String createdDate, double salary,int saved) {
        this.id = id;
        this.title = title;
        this.dept = dept;
        this.desc = desc;
        this.saved = saved;
        this.createdDate = createdDate;
        this.salary = salary;
    }

    protected JobModel(Parcel in) {
        id = in.readInt();
        saved = in.readInt();
        title = in.readString();
        dept = in.readString();
        desc = in.readString();
        createdDate = in.readString();
        salary = in.readDouble();
    }

    public static final Creator<JobModel> CREATOR = new Creator<JobModel>() {
        @Override
        public JobModel createFromParcel(Parcel in) {
            return new JobModel(in);
        }

        @Override
        public JobModel[] newArray(int size) {
            return new JobModel[size];
        }
    };

    public int getSaved() {
        return saved;
    }

    public void setSaved(int saved) {
        this.saved = saved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(saved);
        dest.writeString(title);
        dest.writeString(dept);
        dest.writeString(desc);
        dest.writeString(createdDate);
        dest.writeDouble(salary);
    }
}
