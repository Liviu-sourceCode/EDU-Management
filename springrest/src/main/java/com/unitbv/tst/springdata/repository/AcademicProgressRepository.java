package com.unitbv.tst.springdata.repository;

import com.unitbv.tst.springdata.entity.AcademicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicProgressRepository extends JpaRepository<AcademicProgress, Integer> {
}

