package com.anis.android.medref.main.fragments.tools;

public class ToolItem {
    public int iconResId;
    public String label;

    public String getId() {
        return id;
    }

    public String id;

    public ToolItem(int iconResId, String label,String id) {
        this.iconResId = iconResId;
        this.label = label;
        this.id = id;
    }
}
