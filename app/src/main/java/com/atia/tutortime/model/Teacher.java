package com.atia.tutortime.model;

public class Teacher {
    private String name, email, phone, gender, profilePhoto, password, status, uid;
    private String degree, jobInstitution, educationArea;

    public Teacher(String name, String email, String phone, String gender, String profilePhoto, String password, String status, String uid, String degree, String jobInstitution, String educationArea) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.password = password;
        this.status = status;
        this.uid = uid;
        this.degree = degree;
        this.jobInstitution = jobInstitution;
        this.educationArea = educationArea;
    }

    public Teacher() {
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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getJobInstitution() {
        return jobInstitution;
    }

    public void setJobInstitution(String jobInstitution) {
        this.jobInstitution = jobInstitution;
    }

    public String getEducationArea() {
        return educationArea;
    }

    public void setEducationArea(String educationArea) {
        this.educationArea = educationArea;
    }
}
