package com.example.myfirstapplication.Adapter;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.myfirstapplication.Database.AppDatabase;
import com.example.myfirstapplication.Database.User;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private String DB_NAME = "user";
    AppDatabase appDatabase = Room.databaseBuilder(context,AppDatabase .class,DB_NAME).build();
    List<User> users = appDatabase.userDao().getAll();
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
