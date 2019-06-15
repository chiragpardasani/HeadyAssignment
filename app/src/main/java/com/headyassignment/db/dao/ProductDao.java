package com.headyassignment.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.headyassignment.db.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    LiveData<List<Product>> loadAllProducts();

    @Query("SELECT * FROM products where id IN (:longs)")
    LiveData<List<Product>> loadAllProductsByIds(List<Long> longs);

    @Query("SELECT * FROM products where category_id = :id")
    LiveData<List<Product>> loadProductsByCategory(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);
}
