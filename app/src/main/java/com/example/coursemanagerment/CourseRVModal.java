package com.example.coursemanagerment;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseRVModal implements Parcelable {
    private String userID;
    private String courseName;
    private String courseDescription;
    private String slot;
    private String semester;
    private String courseImg;
    private String courseLink;
    private String courseID;

    public CourseRVModal() {
    }

    protected CourseRVModal(Parcel in) {
        userID = in.readString();
        courseName = in.readString();
        courseDescription = in.readString();
        slot = in.readString();
        semester = in.readString();
        courseImg = in.readString();
        courseLink = in.readString();
        courseID = in.readString();
    }

    public static final Creator<CourseRVModal> CREATOR = new Creator<CourseRVModal>() {
        @Override
        public CourseRVModal createFromParcel(Parcel in) {
            return new CourseRVModal(in);
        }

        @Override
        public CourseRVModal[] newArray(int size) {
            return new CourseRVModal[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseSlot() {
        return slot;
    }

    public void setCourseSlot(String coursePrice) {
        this.slot = coursePrice;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public String getCourseLink() {
        return courseLink;
    }

    public void setCourseLink(String courseLink) {
        this.courseLink = courseLink;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public CourseRVModal(String userID, String courseName, String courseDescription, String slot, String semester, String courseImg, String courseLink, String courseID) {
        this.userID = userID;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.slot = slot;
        this.semester = semester;
        this.courseImg = courseImg;
        this.courseLink = courseLink;
        this.courseID = courseID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userID);
        parcel.writeString(courseName);
        parcel.writeString(courseDescription);
        parcel.writeString(slot);
        parcel.writeString(semester);
        parcel.writeString(courseImg);
        parcel.writeString(courseLink);
        parcel.writeString(courseID);
    }
}
