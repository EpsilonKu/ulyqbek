package kz.bitter.ulyqbek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.bitter.ulyqbek.model.Users;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmailOrUsername (String email,String username);
    List<Users> findAll ();
    List<Users> findByGroupsId (Long id);
}
