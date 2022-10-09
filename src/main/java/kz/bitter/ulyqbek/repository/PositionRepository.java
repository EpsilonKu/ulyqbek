package kz.bitter.ulyqbek.repository;

import org.springframework.stereotype.Repository;

import kz.bitter.ulyqbek.model.LastPosition;
import kz.bitter.ulyqbek.model.Lessons;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PositionRepository extends JpaRepository<LastPosition, Long> {
  public LastPosition findByUsersIdAndCoursesId(Long userId, Long courseId);
}
