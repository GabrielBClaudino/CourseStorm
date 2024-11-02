package com.unifacisa.coursestorm.Controllers.Course;

import com.unifacisa.coursestorm.Models.Category.Category;
import com.unifacisa.coursestorm.Models.Course.Course;
import com.unifacisa.coursestorm.Models.Course.DTO.CourseCreateDTO;
import com.unifacisa.coursestorm.Services.Course.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Cria um novo curso",
            description = "Adiciona um novo curso ao sistema com base nos dados fornecidos.",
            method = "POST"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Categoria inválida ou não encontrada!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!")
    })
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody CourseCreateDTO courseCreateDTO) {
        Optional<Category> optionalCategory = courseService.findCategoryById(courseCreateDTO.getCategoryId());
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Course course = new Course();
        course.setName(courseCreateDTO.getName());
        course.setVideoUrl(courseCreateDTO.getVideoUrl());
        course.setPdfUrl(courseCreateDTO.getPdfUrl());
        course.setCategory(optionalCategory.get());

        Course createdCourse = courseService.save(course);
        return ResponseEntity.created(null).body(createdCourse);
    }

    @Operation(
            summary = "Atualiza um curso existente",
            description = "Atualiza as informações de um curso específico com base no ID fornecido.",
            method = "PUT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado!"),
            @ApiResponse(responseCode = "400", description = "Categoria inválida ou não encontrada!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody CourseCreateDTO courseCreateDTO) {
        Optional<Course> optionalCourse = courseService.findById(id);
        if (optionalCourse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Category> optionalCategory = courseService.findCategoryById(courseCreateDTO.getCategoryId());
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Course existingCourse = optionalCourse.get();
        existingCourse.setName(courseCreateDTO.getName());
        existingCourse.setVideoUrl(courseCreateDTO.getVideoUrl());
        existingCourse.setPdfUrl(courseCreateDTO.getPdfUrl());
        existingCourse.setCategory(optionalCategory.get());

        Course updatedCourse = courseService.save(existingCourse);
        return ResponseEntity.ok(updatedCourse);
    }

    @Operation(
            summary = "Lista todos os cursos",
            description = "Retorna uma lista de todos os cursos disponíveis no sistema.",
            method = "GET"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!")
    })
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAll();
    }

    @Operation(
            summary = "Busca um curso por ID",
            description = "Retorna os detalhes de um curso específico com base no ID fornecido.",
            method = "GET"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Deleta um curso por ID",
            description = "Remove um curso específico do sistema com base no ID fornecido.",
            method = "DELETE"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
