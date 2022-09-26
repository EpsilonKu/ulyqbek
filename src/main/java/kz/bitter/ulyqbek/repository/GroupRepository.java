package kz.bitter.ulyqbek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.bitter.ulyqbek.model.Groups;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Groups, Long> {
    List<Groups> findAll ();
}
