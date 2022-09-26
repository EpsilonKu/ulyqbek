package kz.bitter.ulyqbek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kz.bitter.ulyqbek.model.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long>{

}
