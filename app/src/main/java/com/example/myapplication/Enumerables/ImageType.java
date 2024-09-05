package com.example.myapplication.Enumerables;

public enum ImageType {
    BUSINESS("business_image", "bus_id"),
    REVIEW("review_image", "review_id"),
    CUSTOMER("customer_image", "customer_id"),
    OWNER("owner_image", "owner_id");

    private final String tableName;
    private final String foreignKey;

    ImageType(String tableName , String foreignKey) {
        this.tableName = tableName;
        this.foreignKey = foreignKey;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getForeignKey() {
        return this.foreignKey;
    }


}