package ch.cern.todo.repository;

import ch.cern.todo.models.TaskCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TaskCategoryRepositoryTest {

    @Autowired
    private TaskCategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    public void TestCategorySave(){
        TaskCategory category = new TaskCategory("Test");
        var response = categoryRepository.save(category);
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(
                category.getCategoryName(),
                response.getCategoryName());
    }

    @Test
    public void TestCategorySaveMultiple(){
        TaskCategory category1 = new TaskCategory("Test");
        var response1 = categoryRepository.save(category1);
        Assertions.assertEquals(
                category1.getCategoryName(),
                response1.getCategoryName());
        Assertions.assertEquals(1, categoryRepository.count());

        TaskCategory category2 = new TaskCategory("Test 2");
        var response2 = categoryRepository.save(category2);
        Assertions.assertEquals(
                category2.getCategoryName(),
                response2.getCategoryName());
        Assertions.assertEquals(2, categoryRepository.count());
    }

    @Test
    public void TestCategoryFindById() {
        var category = categoryRepository.save(new TaskCategory("Test"));
        Assertions.assertEquals(1, categoryRepository.count(),
                "Failed creating category during setup");

        var result = categoryRepository.findById(category.getCategoryId());

        Assertions.assertNotNull(result);
    }

    @Test
    public void TestCategoryFindAllCategories() {
        for (int i = 0; i < 10; i++) {
            categoryRepository.save(new TaskCategory("Test"+i));
        }

        var result = categoryRepository.findAll();

        Assertions.assertEquals(10, result.size());
    }

    @Test
    public void TestCategoryDelete() {
        var category = categoryRepository.save(new TaskCategory("Test"));
        Assertions.assertEquals(1, categoryRepository.count(),
                "Failed creating category during setup");

        categoryRepository.delete(category);
        Assertions.assertEquals(0, categoryRepository.count(),
                "category was not deleted");
    }

    @Test
    public void TestCategoryUpdateCategory(){
        var setupCat = categoryRepository.save(new TaskCategory("Test"));
        Assertions.assertEquals(1, categoryRepository.count(),
                "Failed creating category during setup");

        var category = categoryRepository.getById(setupCat.getCategoryId());
        category.setCategoryName("Updated name");
        category.setCategoryDescription("Lorem ipsum");

        var result = categoryRepository.save(category);
        Assertions.assertEquals(1, categoryRepository.count(),
                "A new entry was created instead of updating");
        Assertions.assertEquals(category.getCategoryId(),
                result.getCategoryId());
        Assertions.assertEquals(category.getCategoryName(),
                result.getCategoryName());
        Assertions.assertEquals(category.getCategoryDescription(),
                result.getCategoryDescription());

    }
}
