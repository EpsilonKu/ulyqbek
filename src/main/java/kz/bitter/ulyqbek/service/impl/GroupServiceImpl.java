package kz.bitter.ulyqbek.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import kz.bitter.ulyqbek.model.Courses;
import kz.bitter.ulyqbek.model.Groups;
import kz.bitter.ulyqbek.repository.GroupRepository;
import kz.bitter.ulyqbek.service.GroupService;

@Service
@EnableWebSecurity
public class GroupServiceImpl implements GroupService {
  @Autowired
  GroupRepository groupRepository;

  @Override
  public List<Groups> getAllGroups() {
    return groupRepository.findAll();
  }

  @Override
  public Groups getGroupById(Long id) {
    return groupRepository.getOne(id);
  }

  @Override
  public Groups saveGroups(Groups groups) {
    return groupRepository.save(groups);
  }

  @Override
  public void removeGroups(Groups groups) {
    groupRepository.delete(groups);
  }

  @Override
  public Groups saveCourseToGroup(Groups group, Courses course) {
    group.getCourses().add(course);
    return groupRepository.save(group);
  }

  @Override
  public void kickCourseFromGroup(Groups group, Courses course) {
    group.getCourses().remove(course);
    groupRepository.save(group);
  }
}
