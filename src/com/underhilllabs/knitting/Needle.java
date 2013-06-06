package com.underhilllabs.knitting;

/**
 * Created by bart on 6/4/13.
 */
public class Needle {
    private int mId;
    private String mSize;
    private int mSize_i;
    private String mType;
    private String mMaterial;
    private String mLength;
    private int mLength_i;
    private boolean isMetric;
    private boolean isInUse;
    private String mNotes;

    public Needle(String mSize, int mSize_i, String mType, int mLength_i, String mMaterial, String mLength, boolean isMetric, boolean inUse, String mNotes) {
        //this.mId = mId;
        this.mSize = mSize;
        this.mSize_i = mSize_i;
        this.mType = mType;
        this.mLength_i = mLength_i;
        this.mMaterial = mMaterial;
        this.mLength = mLength;
        this.isMetric = isMetric;
        this.isInUse = inUse;
        this.mNotes = mNotes;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
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

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public int getmLength_i() {
        return mLength_i;
    }

    public void setmLength_i(int mLength_i) {
        this.mLength_i = mLength_i;
    }

    public String getmMaterial() {
        return mMaterial;
    }

    public void setmMaterial(String mMaterial) {
        this.mMaterial = mMaterial;
    }

    public String getmLength() {
        return mLength;
    }

    public void setmLength(String mLength) {
        this.mLength = mLength;
    }

    public boolean isMetric() {
        return isMetric;
    }

    public void setMetric(boolean metric) {
        isMetric = metric;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public void setInUse(boolean inUse) {
        isInUse = inUse;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }
}
