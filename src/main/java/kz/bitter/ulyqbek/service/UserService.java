
package kz.bitter.ulyqbek.service;

import kz.bitter.ulyqbek.model.Groups;
import kz.bitter.ulyqbek.model.Roles;
import kz.bitter.ulyqbek.model.Users;

import java.util.List;

public interface UserService {
  Users getUserByEmailorUsername(String email, String username);

  Users registerUser(Users newUser);

  Users saveUser(Users user);

  Users saveUserToGroup(Users users, Groups groups);

  void removeUserById(Long id);

  List<Users> getAllUsers();

  List<Users> getAllUsersByGroupId(Long id);

  List<Roles> getAllRoles();

  Roles getRole(Long id);

  void kickUserFromGroup(Users users, Groups groups);

  Users getUserById(Long id);

}
