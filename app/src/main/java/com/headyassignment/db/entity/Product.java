package com.headyassignment.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.headyassignment.db.converters.TaxTypeConverter;

@Entity(tableName = "products")
@TypeConverters(TaxTypeConverter.class)
public class Product {

    @PrimaryKey
    long id;
    String name;
    String date_added;
    long category_id;
    Tax tax;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate_added() {
        return date_added;
    }

    public long getCategory_id() {
        return category_id;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }
}
