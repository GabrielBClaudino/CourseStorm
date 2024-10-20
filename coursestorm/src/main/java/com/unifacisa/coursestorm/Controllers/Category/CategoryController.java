package com.unifacisa.coursestorm.Controllers.Category;

import com.unifacisa.coursestorm.Models.Category.Category;
import com.unifacisa.coursestorm.Models.Category.DTO.CategoryCreateDTO;
import com.unifacisa.coursestorm.Services.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryCreateDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        // Se o parentId estiver presente, você pode buscar a categoria pai e definir
        if (categoryDTO.getParentId() != null) {
            Category parent = categoryService.findById(categoryDTO.getParentId());
            category.setParent(parent);
        }
        Category createdCategory = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PostMapping("/{parentId}/children")
    public ResponseEntity<Category> addChildCategory(@PathVariable Long parentId, @RequestBody CategoryCreateDTO childDTO) {
        Category child = new Category();
        child.setName(childDTO.getName());
        categoryService.addChild(parentId, child);
        return ResponseEntity.created(null).body(child);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryCreateDTO categoryDTO) {
        return categoryService.findById(id) != null ?
                ResponseEntity.ok(categoryService.save(new Category(categoryDTO.getName(), null))) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


