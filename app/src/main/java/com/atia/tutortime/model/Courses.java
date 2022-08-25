package com.atia.tutortime.model;

import java.io.Serializable;

public class Courses implements Serializable {

    String courseName, cClass, startDate, endDate, media, platformAddress, totalSeat, availableSeat, studentList, requestList, finishedStatus, cId, teacherId, courseFee;

    public Courses(String courseName, String cClass, String startDate, String endDate, String media, String platformAddress, String totalSeat, String availableSeat, String studentList, String requestList, String finishedStatus, String cId, String teacherId, String courseFee) {
        this.courseName = courseName;
        this.cClass = cClass;
        this.startDate = startDate;
        this.endDate = endDate;
        this.media = media;
        this.platformAddress = platformAddress;
        this.totalSeat = totalSeat;
        this.availableSeat = availableSeat;
        this.studentList = studentList;
        this.requestList = requestList;
        this.finishedStatus = finishedStatus;
        this.cId = cId;
        this.teacherId = teacherId;
        this.courseFee = courseFee;
    }

    public Courses() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getcClass() {
        return cClass;
    }

    public void setcClass(String cClass) {
        this.cClass = cClass;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getPlatformAddress() {
        return platformAddress;
    }

    public void setPlatformAddress(String platformAddress) {
        this.platformAddress = platformAddress;
    }

    public String getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(String totalSeat) {
        this.totalSeat = totalSeat;
    }

    public String getAvailableSeat() {
        return availableSeat;
    }

    public void setAvailableSeat(String availableSeat) {
        this.availableSeat = availableSeat;
    }

    public String getStudentList() {
        return studentList;
    }

    public void setStudentList(String studentList) {
        this.studentList = studentList;
    }

    public String getRequestList() {
        return requestList;
    }

    public void setRequestList(String requestList) {
        this.requestList = requestList;
    }

    public String getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(String finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(String courseFee) {
        this.courseFee = courseFee;
    }
}
