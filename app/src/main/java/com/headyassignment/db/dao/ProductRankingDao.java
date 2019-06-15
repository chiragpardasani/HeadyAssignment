package com.headyassignment.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.headyassignment.db.entity.ProductRanking;

import java.util.List;

@Dao
public interface ProductRankingDao {

    @Query("SELECT * FROM product_rankings group by ranking")
    List<ProductRanking> loadAllProductRankingGroupBy();

    @Query("SELECT * FROM product_rankings where product_id = :id")
    LiveData<List<ProductRanking>> loadAllProductRankingByProductId(long id);

    @Query("SELECT * FROM product_rankings where ranking = :ranking")
    LiveData<List<ProductRanking>> loadAllProductRankingByRanking(String ranking);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductRanking> productRankings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductRanking productRanking);

    @Query("DELETE FROM product_rankings")
    void deleteModel();
}
