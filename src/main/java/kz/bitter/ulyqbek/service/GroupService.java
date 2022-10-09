package kz.bitter.ulyqbek.service;

import java.util.List;

import kz.bitter.ulyqbek.model.Courses;
import kz.bitter.ulyqbek.model.Groups;

public interface GroupService {
  List<Groups> getAllGroups();

  Groups getGroupById(Long id);

  Groups saveGroups(Groups groups);

  Groups saveCourseToGroup(Groups group, Courses course);

  void kickCourseFromGroup(Groups group, Courses course);

  void removeGroups(Groups groups);
}
