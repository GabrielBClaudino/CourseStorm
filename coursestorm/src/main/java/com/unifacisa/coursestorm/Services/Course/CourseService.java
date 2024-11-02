package com.unifacisa.coursestorm.Services.Course;

import com.unifacisa.coursestorm.Models.Category.Category;
import com.unifacisa.coursestorm.Models.Course.Course;
import com.unifacisa.coursestorm.Repositories.Category.CategoryRepository;
import com.unifacisa.coursestorm.Repositories.Course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Category> findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }
}
