package kz.bitter.ulyqbek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.bitter.ulyqbek.model.Tests;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface TestRepository extends JpaRepository <Tests,Long> {
    List<Tests> findByChapterId (Long id);
    void deleteAllByChapterCourseId (Long id);
    void deleteAllByChapterId (Long id);
}
