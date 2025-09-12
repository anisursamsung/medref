package com.anis.android.medref.main.fragments.quickref;

public class ListItem {
    private String label;
    private String description;
    private boolean isPredef;
    private boolean isModel;
    private boolean isUser;
    private String key;

    public int getIconResId() {
        return iconResId;
    }

    private int iconResId;

    public ListItem(String label, String description, boolean isPredef, boolean isModel, boolean isUser, String key, int iconResId) {
        this.label = label;
        this.description= description;
        this.isPredef = isPredef;
        this.isModel = isModel;
        this.isUser = isUser;
        this.key = key;
        this.iconResId = iconResId;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPredef() {
        return isPredef;
    }

    public boolean isModel() {
        return isModel;
    }

    public boolean isUser() {
        return isUser;
    }

    public String getKey() {
        return key;
    }
}
