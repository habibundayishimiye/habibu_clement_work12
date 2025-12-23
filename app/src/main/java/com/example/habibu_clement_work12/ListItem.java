package com.example.habibu_clement_work12;

public class ListItem {
    private String name;
    private String price;
    private String icon;

    public ListItem(String name, String price, String icon) {
        this.name = name;
        this.price = price;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

