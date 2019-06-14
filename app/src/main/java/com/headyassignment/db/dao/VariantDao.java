package com.headyassignment.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.headyassignment.db.entity.Variant;

import java.util.List;

@Dao
public interface VariantDao {

    @Query("SELECT * FROM variants")
    LiveData<List<Variant>> loadAllVariants();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Variant> variants);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Variant variant);
}
