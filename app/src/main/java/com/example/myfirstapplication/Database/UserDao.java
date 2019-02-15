package com.example.myfirstapplication.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> getUserById(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first")
    List<User> getUserByName(String first);

    @Query("SELECT * FROM user WHERE phone_number LIKE :phone")
    User getUserByPhone(String phone);

    @Insert
    void createUser(User... users);

    @Query("DELETE FROM user ")
    void deleteUser();

    @Update
    void updateUser(User user);




}
