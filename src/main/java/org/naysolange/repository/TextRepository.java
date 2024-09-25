package org.naysolange.repository;

import org.naysolange.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TextRepository extends JpaRepository<Text, Long> {
    @Query(value = "SELECT * FROM text ORDER BY RAND() LIMIT :amount", nativeQuery = true)
    List<Text> findLimited(@Param("amount") int amount);
}
