package ch.cern.todo.repository;

import ch.cern.todo.models.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Integer> {

    Boolean existsByCategoryName(String categoryName);
}
