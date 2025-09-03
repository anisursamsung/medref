package com.anis.android.medref.main;

public class ReferenceItem {
    private String title;
    private Class<?> targetActivity;
    private int iconResId;
    private int noteId = -1; // only for user-created notes



    private boolean isModel;
    private int imageResId;
    private int mdFileResId;

    public String getToolbarTitle() {
        return toolbarTitle;
    }

    private String toolbarTitle;
    public ReferenceItem(String title, Class<?> targetActivity, int iconResId) {
        this.title = title;
        this.targetActivity = targetActivity;
        this.iconResId = iconResId;
    }

    public ReferenceItem(String title, Class<?> targetActivity, int iconResId, int noteId) {
        this.title = title;
        this.targetActivity = targetActivity;
        this.iconResId = iconResId;
        this.noteId = noteId;
    }
    public ReferenceItem(String title, Class<?> activityClass, int iconRes, boolean isModel, int imageResId, int mdFileResId, String toolBarTitle) {
        this.title = title;
        this.targetActivity = activityClass;
        this.iconResId = iconRes;
        this.isModel = isModel;
        this.imageResId = imageResId;
        this.mdFileResId = mdFileResId;
        this.toolbarTitle = toolBarTitle;

    }



    public String getTitle() { return title; }
    public Class<?> getTargetActivity() { return targetActivity; }
    public int getIconResId() { return iconResId; }
    public int getNoteId() { return noteId; }

    public int getImageResId() {
        return imageResId;
    }
    public int getMdFileResId() {
        return mdFileResId;
    }
    public boolean isModel() {
        return isModel;
    }
}
