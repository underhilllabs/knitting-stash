package com.underhilllabs.knitting;

/**
 * Created by bart on 6/4/13.
 */
public class Counter {
    private int mId;
    private String mTitle;
    private int mValue;
    private boolean isAscending;
    private int mStep;

    public Counter(String mTitle, int mValue, boolean ascending, int mStep, String mNotes) {
        this.mTitle = mTitle;
        this.mValue = mValue;
        isAscending = ascending;
        this.mStep = mStep;
        this.mNotes = mNotes;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmValue() {
        return mValue;
    }

    public void setmValue(int mValue) {
        this.mValue = mValue;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }

    public int getmStep() {
        return mStep;
    }

    public void setmStep(int mStep) {
        this.mStep = mStep;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    private String mNotes;
}
