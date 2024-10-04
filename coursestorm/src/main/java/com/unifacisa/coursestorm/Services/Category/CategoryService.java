package com.unifacisa.coursestorm.Services.Category;

import com.unifacisa.coursestorm.Models.Category.Category;
import com.unifacisa.coursestorm.Repositories.Category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Método para listar todos as Categorias
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    // Método para criar ou atualizar uma Categoria
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Método para buscar uma Categoria pelo ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Método para deletar uma Categoria pelo ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
