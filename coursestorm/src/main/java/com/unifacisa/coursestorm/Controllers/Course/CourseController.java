package com.unifacisa.coursestorm.Controllers.Course;

import com.unifacisa.coursestorm.Models.Category.Category;
import com.unifacisa.coursestorm.Models.Course.Course;
import com.unifacisa.coursestorm.Services.Category.CategoryService;
import com.unifacisa.coursestorm.Services.Course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    // Listar todos os cursos
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Criar um novo curso associado a uma categoria
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        // Valida se a categoria fornecida existe
        if (course.getCategory() != null && course.getCategory().getId() != null) {
            Optional<Category> category = categoryService.getCategoryById(course.getCategory().getId());
            if (category.isPresent()) {
                course.setCategory(category.get());
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        }
        Course createdCourse = courseService.saveCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    // Buscar um curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar um curso e sua categoria associada
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        Optional<Course> courseOptional = courseService.getCourseById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setName(courseDetails.getName());
            course.setVideoUrl(courseDetails.getVideoUrl());
            course.setPdfUrl(courseDetails.getPdfUrl());

            // Verifica e atualiza a categoria associada, se fornecida
            if (courseDetails.getCategory() != null && courseDetails.getCategory().getId() != null) {
                Optional<Category> category = categoryService.getCategoryById(courseDetails.getCategory().getId());
                if (category.isPresent()) {
                    course.setCategory(category.get());
                } else {
                    return ResponseEntity.badRequest().body(null);
                }
            }

            Course updatedCourse = courseService.saveCourse(course);
            return ResponseEntity.ok(updatedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar um curso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (courseService.getCourseById(id).isPresent()) {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
