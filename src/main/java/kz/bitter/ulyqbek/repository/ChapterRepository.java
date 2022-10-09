package kz.bitter.ulyqbek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.bitter.ulyqbek.model.Chapters;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ChapterRepository extends JpaRepository<Chapters, Long> {
  List<Chapters> findByCourseId(Long id);

  void deleteAllByCourseId(Long id);

  Chapters findByOrderPlace(Long orderPlace);
}
