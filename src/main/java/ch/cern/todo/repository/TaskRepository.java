package ch.cern.todo.repository;


import ch.cern.todo.models.Task;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.sql.Timestamp;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer>, QueryByExampleExecutor<Task> {

    /*NOTE: The dates start at 0:00, so to get a specific day, you need
        deadline between from and from +1 day
        i.e. deadline between 2025/02/05 and 2025/02/06  will give 2025/02/05
    */

    @Query("select t from Task t where t.deadline >= :from and t.deadline < :to")
    List<Task> findAllTasksBetweenDates(@Param("from") Timestamp from,
                                        @Param("to") Timestamp to);

    @Query("select t from Task t where t.deadline >= :from and t.deadline < :to")
    Page<Task> findAllTasksBetweenDates(Pageable pageable,
                                        @Param("from") Timestamp from,
                                        @Param("to") Timestamp to);

    @Query("select t from Task t where t.deadline >= :from and t.deadline < :to")
    Page<Task> findAllTasksBetweenDates(Example<Task> example,
                                        Pageable pageable,
                                        @Param("from") Timestamp from,
                                        @Param("to") Timestamp to);
}
