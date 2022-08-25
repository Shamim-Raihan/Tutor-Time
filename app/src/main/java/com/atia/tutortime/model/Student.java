package com.atia.tutortime.model;

public class Student{
    private String name, email, phone, gender, profilePhoto, password, status, uid;
    private String sClass, sAge;

    public Student(String name, String email, String phone, String gender, String profilePhoto, String password, String status, String uid, String sClass, String sAge) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.password = password;
        this.status = status;
        this.uid = uid;
        this.sClass = sClass;
        this.sAge = sAge;
    }

    public Student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getsAge() {
        return sAge;
    }

    public void setsAge(String sAge) {
        this.sAge = sAge;
    }
}
