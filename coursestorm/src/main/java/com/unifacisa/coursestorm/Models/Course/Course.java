package com.unifacisa.coursestorm.Models.Course;

import com.unifacisa.coursestorm.Models.Category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String videoUrl;
    private String pdfUrl;

    // Relacionamento com Category: Muitos Cursos para Uma Categoria
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
