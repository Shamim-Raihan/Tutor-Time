package com.atia.tutortime.model;

public class Enrollment {

    String id, courseId, studentId, teacherId, approveStatus;

    public Enrollment(String id, String courseId, String studentId, String teacherId, String approveStatus) {
        this.id = id;
        this.courseId = courseId;
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.approveStatus = approveStatus;
    }

    public Enrollment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }
}
