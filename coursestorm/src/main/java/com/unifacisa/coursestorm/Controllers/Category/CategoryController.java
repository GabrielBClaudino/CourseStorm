package com.unifacisa.coursestorm.Controllers.Category;

import com.unifacisa.coursestorm.Models.Category.Category;
import com.unifacisa.coursestorm.Models.Category.DTO.CategoryCreateDTO;
import com.unifacisa.coursestorm.Services.Category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Lista todas as Categorias",
            description = "Retorna uma lista de todas as categorias disponíveis no sistema.",
            method = "GET"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!")
    })
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }
    @Operation(
            summary = "Busca uma Categoria por ID",
            parameters = {
                    @Parameter(name = "id", description = "ID da categoria a ser buscada", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
            },
            method = "GET"
    )
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Buscado com sucesso!"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválidos!"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Cria uma nova Categoria",
            description = "Endpoint para criar uma nova categoria, podendo associá-la a uma categoria pai.",
            parameters = {
                    @Parameter(name = "categoryDTO", description = "Dados da nova categoria", required = true, schema = @Schema(implementation = CategoryCreateDTO.class))
            },
            method = "POST"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso!"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválidos!"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado!")
    })
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

    @Operation(
            summary = "Adiciona uma Subcategoria a uma Categoria Pai",
            description = "Cria uma nova subcategoria associada a uma categoria pai especificada pelo ID.",
            parameters = {
                    @Parameter(name = "parentId", description = "ID da categoria pai à qual a subcategoria será associada", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64")),
                    @Parameter(name = "childDTO", description = "Dados da subcategoria a ser criada", required = true, schema = @Schema(implementation = CategoryCreateDTO.class))
            },
            method = "POST"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subcategoria criada com sucesso!"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválidos!"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado!")
    })
    @PostMapping("/{parentId}/children")
    public ResponseEntity<Category> addChildCategory(@PathVariable Long parentId, @RequestBody CategoryCreateDTO childDTO) {
        Category child = new Category();
        child.setName(childDTO.getName());
        categoryService.addChild(parentId, child);
        return ResponseEntity.created(null).body(child);
    }

    @Operation(
            summary = "Atualiza uma Categoria existente",
            description = "Atualiza uma categoria existente com base no ID fornecido.",
            parameters = {
                    @Parameter(name = "id", description = "ID da categoria a ser atualizada", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64")),
                    @Parameter(name = "categoryDTO", description = "Dados atualizados da categoria", required = true, schema = @Schema(implementation = CategoryCreateDTO.class))
            },
            method = "PUT"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada!"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválidos!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado!")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryCreateDTO categoryDTO) {
        return categoryService.findById(id) != null ?
                ResponseEntity.ok(categoryService.save(new Category(categoryDTO.getName(), null))) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Exclui uma Categoria",
            description = "Exclui uma categoria com base no ID fornecido.",
            parameters = {
                    @Parameter(name = "id", description = "ID da categoria a ser excluída", required = true, in = ParameterIn.PATH, schema = @Schema(type = "integer", format = "int64"))
            },
            method = "DELETE"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada!"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a operação!"),
            @ApiResponse(responseCode = "401", description = "Usuário não autorizado!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


