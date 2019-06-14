package com.headyassignment.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey
    long id;
    String name;
    long parent_id;

    @Ignore
    long[] child_categories;

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getParent_id() {
        return parent_id;
    }

    public long[] getChild_categories() {
        return child_categories;
    }

    public void setChild_categories(long[] child_categories) {
        this.child_categories = child_categories;
    }
}
