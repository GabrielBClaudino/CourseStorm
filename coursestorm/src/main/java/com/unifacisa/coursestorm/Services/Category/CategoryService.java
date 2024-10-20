package com.unifacisa.coursestorm.Services.Category;

import com.unifacisa.coursestorm.Models.Category.Category;
import com.unifacisa.coursestorm.Repositories.Category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public void addChild(Long parentId, Category child) {
        Category parent = findById(parentId);
        if (parent != null) {
            parent.addChild(child);
            save(parent); // Atualiza a categoria pai
        }
    }
}
