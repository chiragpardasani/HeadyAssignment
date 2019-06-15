package com.headyassignment.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.headyassignment.db.entity.Category;
import com.headyassignment.db.entity.Product;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categories where parent_id = :id")
    LiveData<List<Category>> loadCategoriesById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);
}
