package ch.cern.todo.services;

import ch.cern.todo.models.TaskCategory;
import ch.cern.todo.repository.TaskCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskCategoryService {


    private final TaskCategoryRepository categoryRepository;

    public TaskCategoryService(TaskCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<TaskCategory> getTaskCategories(){
        return categoryRepository.findAll();
    }


    public TaskCategory getCategory(int id){
        return categoryRepository.findById(id).orElse(null);
    }

    public TaskCategory createCategory(TaskCategory category){
        categoryRepository.save(category);
        return category;
    }

    public TaskCategory editCategory(int id, TaskCategory category){
        if(!categoryRepository.existsById(id))
            return null;
        if(category.getCategoryId() != id)
            return null;


        categoryRepository.save(category);
        return category;
    }

    public boolean deleteCategory(int id) {
        if(!categoryRepository.existsById(id))
            return false;

        categoryRepository.deleteById(id);
        return true;
    }
}
