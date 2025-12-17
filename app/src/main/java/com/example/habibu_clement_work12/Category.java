package com.example.habibu_clement_work12;

public class Category {
    private String name;
    private String icon;
    private String itemCount;

    public Category(String name, String icon, String itemCount) {
        this.name = name;
        this.icon = icon;
        this.itemCount = itemCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
}




