package com.unifacisa.coursestorm.Controllers.Course;

import com.unifacisa.coursestorm.Models.Category.Category;
import com.unifacisa.coursestorm.Models.Course.Course;
import com.unifacisa.coursestorm.Models.Course.DTO.CourseCreateDTO;
import com.unifacisa.coursestorm.Repositories.Category.CategoryRepository;
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
    private CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseCreateDTO courseCreateDTO) {
        Category category = categoryRepository.findById(courseCreateDTO.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        Course course = new Course();
        course.setName(courseCreateDTO.getName());
        course.setVideoUrl(courseCreateDTO.getVideoUrl());
        course.setPdfUrl(courseCreateDTO.getPdfUrl());
        course.setCategory(category);

        Course createdCourse = courseService.save(course);
        return ResponseEntity.created(null).body(createdCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody CourseCreateDTO courseCreateDTO) {
        // Verifica se o curso existe
        Optional<Course> optionalCourse = courseService.findById(id);
        if (optionalCourse.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna erro se o curso não é encontrado
        }

        Course existingCourse = optionalCourse.get();

        // Busca a categoria associada ao curso
        Category category = categoryRepository.findById(courseCreateDTO.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build(); // Retorna erro se a categoria não existe
        }

        // Atualiza os dados do curso existente
        existingCourse.setName(courseCreateDTO.getName());
        existingCourse.setVideoUrl(courseCreateDTO.getVideoUrl());
        existingCourse.setPdfUrl(courseCreateDTO.getPdfUrl());
        existingCourse.setCategory(category);

        // Salva e retorna o curso atualizado
        Course updatedCourse = courseService.save(existingCourse);
        return ResponseEntity.ok(updatedCourse);
    }



    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

