package com.unifacisa.coursestorm.Repositories.Category;

import com.unifacisa.coursestorm.Models.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
