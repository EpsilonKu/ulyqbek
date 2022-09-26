package kz.bitter.ulyqbek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.bitter.ulyqbek.model.Courses;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CourseRepository extends JpaRepository <Courses, Long> {
}
