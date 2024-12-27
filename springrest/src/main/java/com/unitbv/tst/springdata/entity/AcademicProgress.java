package com.unitbv.tst.springdata.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "academic_progress")
public class AcademicProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "grade")
    private Double grade;

    @Column(name = "attendance_count", nullable = false)
    private Integer attendanceCount;

    public AcademicProgress() {}

    public AcademicProgress(Enrollment enrollment, Double grade, Integer attendanceCount) {
        this.enrollment = enrollment;
        this.grade = grade;
        this.attendanceCount = attendanceCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public Integer getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(Integer attendanceCount) {
        this.attendanceCount = attendanceCount;
    }
}

