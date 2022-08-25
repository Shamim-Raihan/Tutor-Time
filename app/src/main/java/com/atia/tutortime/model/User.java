package com.atia.tutortime.model;

abstract class User {
    private String name, email, phone, gender, profilePhoto, password, status, uid;

    public User(String name, String email, String phone, String gender, String profilePhoto, String password, String status, String uid) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.password = password;
        this.status = status;
        this.uid = uid;
    }

    public User() {
    }

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract String getPhone();

    public abstract void setPhone(String phone);

    public abstract String getGender();

    public abstract void setGender(String gender);

    public abstract String getProfilePhoto();

    public abstract void setProfilePhoto(String profilePhoto);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract String getStatus();

    public abstract void setStatus();

    public abstract String getUid();

    public abstract void setUid();
}
