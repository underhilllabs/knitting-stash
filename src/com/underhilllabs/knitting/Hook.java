package com.underhilllabs.knitting;

/**
 * Created by bart on 6/4/13.
 */
public class Hook {
    private int mId;
    private String mSize;
    private int mSize_i;
    private String mNotes;
    private String mMaterial;

    public Hook(String mSize, int mSize_i, String mNotes, String mMaterial) {
        this.mSize = mSize;
        this.mSize_i = mSize_i;
        this.mNotes = mNotes;
        this.mMaterial = mMaterial;
    }

    public String getmSize() {
        return mSize;
    }

    public void setmSize(String mSize) {
        this.mSize = mSize;
    }

    public int getmSize_i() {
        return mSize_i;
    }

    public void setmSize_i(int mSize_i) {
        this.mSize_i = mSize_i;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public String getmMaterial() {
        return mMaterial;
    }

    public void setmMaterial(String mMaterial) {
        this.mMaterial = mMaterial;
    }
}
