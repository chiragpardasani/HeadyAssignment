package com.headyassignment.utils;

import com.headyassignment.db.entity.Product;
import com.headyassignment.db.entity.Tax;

import java.util.List;

public class Response {

    List<ResponseCategory> categories;
    List<ResponseRanking> rankings;

    public class ResponseCategory {
        long id;
        String name;
        List<ResponseProduct> products;
        long[] child_categories;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<ResponseProduct> getProducts() {
            return products;
        }

        public long[] getChild_categories() {
            return child_categories;
        }
    }

    public class ResponseProduct {
        long id;
        String name;
        String date_added;
        List<ResponseVariant> variants;
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

        public List<ResponseVariant> getVariants() {
            return variants;
        }

        public Tax getTax() {
            return tax;
        }
    }

    public class ResponseVariant {
        long id;
        String color;
        String size;
        String price;

        public long getId() {
            return id;
        }

        public String getColor() {
            return color;
        }

        public String getSize() {
            return size;
        }

        public String getPrice() {
            return price;
        }
    }

    public class ResponseRanking {
        String ranking;
        List<ResponseRankingProduct> products;

        public String getRanking() {
            return ranking;
        }

        public List<ResponseRankingProduct> getProducts() {
            return products;
        }
    }

    public class ResponseRankingProduct {
        long id;
        long view_count;
        long order_count;
        long shares;

        public long getId() {
            return id;
        }

        public long getView_count() {
            return view_count;
        }

        public long getOrder_count() {
            return order_count;
        }

        public long getShares() {
            return shares;
        }

    }

    public List<ResponseCategory> getCategories() {
        return categories;
    }

    public List<ResponseRanking> getRankings() {
        return rankings;
    }
}
