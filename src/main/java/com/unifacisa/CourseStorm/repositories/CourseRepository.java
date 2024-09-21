package com.unifacisa.coursestorm.Repositories;

import com.unifacisa.coursestorm.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
