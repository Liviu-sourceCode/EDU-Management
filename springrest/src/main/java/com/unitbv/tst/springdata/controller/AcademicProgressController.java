package com.unitbv.tst.springdata.controller;

import com.unitbv.tst.springdata.entity.AcademicProgress;
import com.unitbv.tst.springdata.repository.AcademicProgressRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic_progress")
public class AcademicProgressController {
    private final AcademicProgressRepository academicProgressRepository;

    public AcademicProgressController(AcademicProgressRepository academicProgressRepository) {
        this.academicProgressRepository = academicProgressRepository;
    }

    @GetMapping
    public List<AcademicProgress> getAllAcademicProgress() {
        return academicProgressRepository.findAll();
    }

    @GetMapping("/{id}")
    public AcademicProgress getAcademicProgressById(@PathVariable("id") Integer id) {
        return academicProgressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AcademicProgress not found with id " + id));
    }


    @PostMapping
    public AcademicProgress addAcademicProgress(@RequestBody AcademicProgress academicProgress) {
        return academicProgressRepository.save(academicProgress);
    }

    @DeleteMapping("/{id}")
    public void deleteAcademicProgress(@PathVariable("id") Integer id) {
        academicProgressRepository.deleteById(id);
    }
}

