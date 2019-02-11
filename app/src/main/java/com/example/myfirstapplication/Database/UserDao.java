package com.example.myfirstapplication.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> getUserById(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first")
    List<User> getUserByName(String first);

    @Insert
    void createUser(User... users);

    @Delete
    void deleteUser(User user);

}
