package com.unifacisa.coursestorm.Services;

import com.unifacisa.coursestorm.Models.Course;
import com.unifacisa.coursestorm.Repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // Método para listar todos os cursos
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Método para criar ou atualizar um curso
    public Course saveCourse(Course course) {
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
