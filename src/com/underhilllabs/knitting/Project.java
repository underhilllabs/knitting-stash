package com.underhilllabs.knitting;

/**
 * Created by bart on 6/4/13.
 */
public class Project {
    private int mId;
    private String mTitle;
    private String mPicUrl;
    private int mStatus;
    private String mNeededShopping;
    private String mNotes;

    public Project(String mTitle, String mPicUrl, int mStatus, String mNeededShopping, String mNotes) {
        this.mTitle = mTitle;
        this.mPicUrl = mPicUrl;
        this.mStatus = mStatus;
        this.mNeededShopping = mNeededShopping;
        this.mNotes = mNotes;
    }

    public int getmId() {

        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPicUrl() {
        return mPicUrl;
    }

    public void setmPicUrl(String mPicUrl) {
        this.mPicUrl = mPicUrl;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getmNeededShopping() {
        return mNeededShopping;
    }

    public void setmNeededShopping(String mNeededShopping) {
        this.mNeededShopping = mNeededShopping;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNotes(String mNotes) {
        this.mNotes = mNotes;
    }

}
