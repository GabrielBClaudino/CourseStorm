package com.unifacisa.coursestorm.Models.Category;

import com.unifacisa.coursestorm.Models.Course.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;

    // Relacionamento com Course: Uma Categoria para Muitos Cursos
    @OneToMany(mappedBy = "category")
    private List<Course> courses;
}
