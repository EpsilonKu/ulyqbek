package kz.bitter.ulyqbek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.bitter.ulyqbek.model.Lessons;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface LessonRepository extends JpaRepository<Lessons, Long> {
  List<Lessons> findByChapterId(Long id);

  void deleteAllByChapterCourseId(Long id);

  void deleteAllByChapterId(Long id);

  Lessons findByOrderPlaceAndChapterCourseId(Long orderPlace, Long courseId);
}
