package com.anis.android.medref.main.fragments.subjects;


public class SubjectItem {
    private final String label;
    private final int iconResId;


    private final String fileName;

    public SubjectItem(String label, int iconResId, String fileName) {
        this.label = label;
        this.iconResId = iconResId;
        this.fileName = fileName;
    }

    public String getLabel() {
        return label;
    }

    public int getIconResId() {
        return iconResId;
    }
    public String getFileName() {
        return fileName;
    }

}
