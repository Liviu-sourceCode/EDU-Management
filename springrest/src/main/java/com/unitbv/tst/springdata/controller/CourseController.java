package com.unitbv.tst.springdata.controller;

import com.unitbv.tst.springdata.entity.Course;
import com.unitbv.tst.springdata.repository.CourseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable("id") Integer id) {
        return courseRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Course saveCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable("id") Integer id, @RequestBody Course course) {
        course.setId(id);
        return courseRepository.save(course);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable("id") Integer id) {
        courseRepository.deleteById(id);
    }
}
