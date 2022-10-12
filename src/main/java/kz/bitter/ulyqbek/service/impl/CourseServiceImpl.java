package kz.bitter.ulyqbek.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import kz.bitter.ulyqbek.model.Chapters;
import kz.bitter.ulyqbek.model.Courses;
import kz.bitter.ulyqbek.model.LastPosition;
import kz.bitter.ulyqbek.model.Lessons;
import kz.bitter.ulyqbek.model.Tests;
import kz.bitter.ulyqbek.model.Users;
import kz.bitter.ulyqbek.repository.ChapterRepository;
import kz.bitter.ulyqbek.repository.CourseRepository;
import kz.bitter.ulyqbek.repository.LessonRepository;
import kz.bitter.ulyqbek.repository.PositionRepository;
import kz.bitter.ulyqbek.repository.TestRepository;
import kz.bitter.ulyqbek.service.CourseService;
import kz.bitter.ulyqbek.service.UserService;

import java.util.List;

@Service
@EnableWebSecurity
public class CourseServiceImpl implements CourseService {
  @Autowired
  CourseRepository courseRepository;

  @Autowired
  ChapterRepository chapterRepository;

  @Autowired
  PositionRepository positionRepository;

  @Autowired
  UserService userService;
  // @Autowired
  // TasksRepository<Tasks> taskRepository;

  @Autowired
  LessonRepository lessonRepository;

  @Autowired
  TestRepository testRepository;

  @Override
  public void saveCourse(Courses courses) {
    courseRepository.save(courses);
  }

  @Override
  public void saveChapter(Chapters chapter) {
    chapterRepository.save(chapter);
  }

  @Override
  public Lessons saveLesson(Lessons lesson) {
    if (lesson.getOrderPlace() == null) {
      Long orderPlace = lessonRepository.findTopByOrderPlace();
      if (orderPlace != null) {
        lesson.setOrderPlace(lessonRepository.findTopByOrderPlace() + 1);
      }
    }
    return lessonRepository.save(lesson);
  }

  @Override
  public Tests saveTests(Tests test) {
    return testRepository.save(test);
  }

  @Override
  public void removeCourse(Long id) {
    lessonRepository.deleteAllByChapterCourseId(id);
    testRepository.deleteAllByChapterCourseId(id);
    chapterRepository.deleteAllByCourseId(id);
    courseRepository.deleteById(id);
  }

  @Override
  public void removeChapter(Long id) {
    lessonRepository.deleteAllByChapterId(id);
    testRepository.deleteAllByChapterId(id);
    chapterRepository.deleteById(id);
  }

  @Override
  public void removeLesson(Long id) {
    lessonRepository.deleteById(id);
  }

  @Override
  public void removeTest(Long id) {
    testRepository.deleteById(id);
  }

  @Override
  public List<Courses> getAllCourses() {
    return courseRepository.findAll();
  }

  @Override
  public Courses getCourseById(Long id) {
    return courseRepository.getOne(id); // TODO: Remove deprecated
  }

  @Override
  public Chapters getChapterById(Long id) {
    return chapterRepository.getOne(id);
  }

  @Override
  public Lessons getLessonbyId(Long id) {
    return lessonRepository.getOne(id);
  }

  @Override
  public Tests getTestbyId(Long id) {
    return testRepository.getOne(id);
  }

  @Override
  public List<Chapters> getChapterByCourseId(Long id) {
    return chapterRepository.findByCourseId(id);
  }

  @Override
  public List<Lessons> getLessonsByChapterId(Long id) {
    return lessonRepository.findByChapterId(id);
  }

  @Override
  public List<Tests> getTestsByChapterId(Long id) {
    return testRepository.findByChapterId(id);
  }

  @Override
  public Page<Courses> getCoursesByPage(int page) {
    Pageable sortedByName = PageRequest.of(page, 10, Sort.by("name"));
    Page<Courses> result = courseRepository.findAll(sortedByName);

    return result;
  }

  @Override
  public LastPosition getLastPosition(Long userId, Long courseId) {
    LastPosition position = positionRepository.findByUsersIdAndCoursesId(userId, courseId);
    if (position == null) {
      position = new LastPosition(this.getCourseById(courseId),
          lessonRepository.findByOrderPlaceAndChapterCourseId(0L, courseId),
          userService.getUserById(userId));
      positionRepository.save(position);
      return position;
    } else {
      return position;
    }
  }

  @Override
  public void saveLastPosition(Users user, Courses course, Lessons lesson) {
    LastPosition position = this.getLastPosition(user.getId(), course.getId());
    position.setLessons(lesson);
    positionRepository.save(position);
  }
}
