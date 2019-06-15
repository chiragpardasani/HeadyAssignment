package com.headyassignment.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.headyassignment.db.entity.ProductVariantPOJO;
import com.headyassignment.db.entity.Variant;

import java.util.List;

@Dao
public interface VariantDao {

    @Query("SELECT * FROM variants where product_id = :id")
    LiveData<List<Variant>> loadAllVariantsByProduct(long id);

    @Query("SELECT * FROM variants left join products on product_id = products.id group by product_id")
    LiveData<List<ProductVariantPOJO>> loadAllVariantsWithProduct();

    @Query("SELECT * FROM variants left join products on product_id = products.id where product_id IN (:longs) group by product_id")
    LiveData<List<ProductVariantPOJO>> loadAllVariantsWithProduct(List<Long> longs);

    @Query("SELECT * FROM variants left join products on product_id = products.id where category_id = :id group by product_id")
    LiveData<List<ProductVariantPOJO>> loadAllVariantsByCategory(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Variant> variants);
}
