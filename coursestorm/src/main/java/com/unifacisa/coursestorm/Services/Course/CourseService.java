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

    // Método para listar todos os cursos
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Método para criar um novo curso com a categoria associada
    public Course saveCourse(Course course) {
        if (course.getCategory() != null) {
            Optional<Category> category = categoryRepository.findById(course.getCategory().getId());
            if (category.isPresent()) {
                course.setCategory(category.get());
            } else {
                throw new IllegalArgumentException("Category not found");
            }
        }
        return courseRepository.save(course);
    }

    // Método para buscar um curso pelo ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // Método para deletar um curso pelo ID
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
