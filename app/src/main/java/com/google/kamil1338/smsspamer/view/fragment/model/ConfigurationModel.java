package com.google.kamil1338.smsspamer.view.fragment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pierudzki on 2016-06-01.
 */
public class ConfigurationModel implements Parcelable {

    private boolean isModelCorrect = true;
    private int phoneNumbersCount;
    private int intervalFrom;//secs
    private int intervalTo;//secs
    private int remainedNumbers;
    private long waitTime;//milis

    public ConfigurationModel() {
        /** nothing*/
    }

    public ConfigurationModel(int phoneNumbersCount, int intervalFrom, int intervalTo, int remainedNumbers, long waitTime) {
        this.phoneNumbersCount = phoneNumbersCount;
        this.intervalFrom = intervalFrom;
        this.setIntervalTo(intervalTo);
        this.remainedNumbers = remainedNumbers;
        this.waitTime = waitTime;
    }

    public int getPhoneNumbersCount() {
        return phoneNumbersCount;
    }

    public void setPhoneNumbersCount(int phoneNumbersCount) {
        this.phoneNumbersCount = phoneNumbersCount;
    }

    public int getIntervalFrom() {
        return intervalFrom;
    }

    public void setIntervalFrom(int intervalFrom) {
        this.intervalFrom = intervalFrom;
    }

    public int getRemainedNumbers() {
        return remainedNumbers;
    }

    public void setRemainedNumbers(int remainedNumbers) {
        this.remainedNumbers = remainedNumbers;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public int getIntervalTo() {
        return intervalTo;
    }

    public void setIntervalTo(int intervalTo) {
        this.intervalTo = intervalTo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.phoneNumbersCount);
        dest.writeInt(this.intervalFrom);
        dest.writeInt(this.intervalTo);
        dest.writeInt(this.remainedNumbers);
        dest.writeLong(this.waitTime);
    }

    protected ConfigurationModel(Parcel in) {
        this.phoneNumbersCount = in.readInt();
        this.intervalFrom = in.readInt();
        this.intervalTo = in.readInt();
        this.remainedNumbers = in.readInt();
        this.waitTime = in.readLong();
    }

    public static final Parcelable.Creator<ConfigurationModel> CREATOR = new Parcelable.Creator<ConfigurationModel>() {
        @Override
        public ConfigurationModel createFromParcel(Parcel source) {
            return new ConfigurationModel(source);
        }

        @Override
        public ConfigurationModel[] newArray(int size) {
            return new ConfigurationModel[size];
        }
    };

    public boolean isModelCorrect() {
        return isModelCorrect;
    }

    public void setModelCorrect(boolean isModelCorrect) {
        this.isModelCorrect = isModelCorrect;
    }
}
