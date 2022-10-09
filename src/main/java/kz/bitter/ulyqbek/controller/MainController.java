package kz.bitter.ulyqbek.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kz.bitter.ulyqbek.model.Chapters;
import kz.bitter.ulyqbek.model.Lessons;
import kz.bitter.ulyqbek.model.Users;
import kz.bitter.ulyqbek.service.CourseService;
import kz.bitter.ulyqbek.service.UserService;

@Controller
public class MainController {

  @Autowired
  private UserService userService;

  @Autowired
  private CourseService courseService;

  @GetMapping(value = "/")
  @PreAuthorize("isAnonymous()")
  public String index(Model model) {

    // model.addAttribute("gender", "");
    // model.addAttribute("currentUser", getUserDaa());
    return "guest/index";
  }

  @GetMapping(value = "/courses/{page}")
  @PreAuthorize("isAuthenticated()")
  public String exploreCourse(Model model, @PathVariable("page") int page) {
    model.addAttribute("allCourses", courseService.getCoursesByPage(page));
    model.addAttribute("currentUser", getUserData());
    model.addAttribute("currentPage", page);
    return "user/course/list";
  }

  @GetMapping(value = "/course/{id}")
  @PreAuthorize("isAuthenticated()")
  public String courseView(@PathVariable("id") Long id,
      Model model) {
    model.addAttribute("currentUser", getUserData());
    model.addAttribute("currentCourse", courseService.getCourseById(id));
    return "user/course/view";
  }

  @GetMapping(value = "/course/view/{id}/{lessonId}")
  @PreAuthorize("isAuthenticated()")
  public String courseLearn(@PathVariable Long id,
      @PathVariable Long lessonId,
      Model model) {
    // TODO: refactor and remove chapterId
    model.addAttribute("currentUser", getUserData());
    model.addAttribute("currentCourse", courseService.getCourseById(id));
    List<Chapters> chaptersList = courseService.getChapterByCourseId(id);
    model.addAttribute("currentChapterList", chaptersList);
    // model.addAttribute("currentChapter", eService.(lessonId));
    model.addAttribute("currentLesson", courseService.getLessonbyId(lessonId));
    List<List<Lessons>> lessonsList = new ArrayList<>();
    for (Chapters i : chaptersList) {
      List<Lessons> lessons = courseService.getLessonsByChapterId(i.getId());
      lessonsList.add(lessons);
    }
    courseService.saveLastPosition(getUserData(), courseService.getCourseById(id),
        courseService.getLessonbyId(lessonId));
    model.addAttribute("currentLessonList", lessonsList);
    model.addAttribute("currentUser", getUserData());
    return "user/course/learn";
  }

  @GetMapping(value = "/course/view/{id}")
  @PreAuthorize("isAuthenticated()")
  public String courseLearn(@PathVariable Long id,
      Model model) {
    model.addAttribute("currentUser", getUserData());
    model.addAttribute("currentCourse", courseService.getCourseById(id));
    List<Chapters> chaptersList = courseService.getChapterByCourseId(id);
    model.addAttribute("currentChapterList", chaptersList);
    Lessons currentLesson = courseService.getLastPosition(getUserData().getId(), id).getLessons();
    model.addAttribute("currentLesson", currentLesson);
    model.addAttribute("currentChapter", currentLesson.getChapter());
    List<List<Lessons>> lessonsList = new ArrayList<>();
    for (Chapters i : chaptersList) {
      List<Lessons> lessons = courseService.getLessonsByChapterId(i.getId());
      lessonsList.add(lessons);
    }
    model.addAttribute("currentLessonList", lessonsList);
    model.addAttribute("currentUser", getUserData());
    return "user/course/learn";
  }

  @GetMapping(value = "/profile")
  @PreAuthorize("isAuthenticated()")
  public String profile(Model model) {
    model.addAttribute("currentUser", getUserData());
    return "user/profile";
  }

  private Users getUserData() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      User secUser = (User) authentication.getPrincipal();
      return userService.getUserByEmailorUsername(secUser.getUsername(), secUser.getUsername());
    }

    return null;
  }
}
