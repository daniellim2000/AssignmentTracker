package com.jeffsieu.tasktracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Jeff on 26/6/2016.
 */
public class Task implements Parcelable {
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
    private Calendar date;
    private String name;

    public Task() {
        date = GregorianCalendar.getInstance();
        name = "";
    }

    public Calendar getDate() {
        return date;
    }

    public String getDateString() {
        Calendar today = GregorianCalendar.getInstance();
        if (date.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            if (date.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                int todayDay = today.get(Calendar.DAY_OF_MONTH);
                int dateDay = date.get(Calendar.DAY_OF_MONTH);
                if (dateDay == todayDay) {
                    return "Today";
                }
                else if (dateDay == todayDay + 1) {
                    return "Tomorrow";
                }
            }
            if (date.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) < 7 && date.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 0) {
                switch (date.get(Calendar.DAY_OF_WEEK)) {
                    case 0:
                        return "Saturday";
                    case 1:
                        return "Sunday";
                    case 2:
                        return "Monday";
                    case 3:
                        return "Tuesday";
                    case 4:
                        return "Wednesday";
                    case 5:
                        return "Thursday";
                    case 6:
                        return "Friday";
                }
            }
        }
        format.setCalendar(date);
        String dateFormatted = format.format(date.getTime());
        return dateFormatted;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Task(Parcel in) {
        date = (Calendar) in.readValue(Calendar.class.getClassLoader());
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}