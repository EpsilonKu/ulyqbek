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
import kz.bitter.ulyqbek.model.Courses;
import kz.bitter.ulyqbek.model.Groups;
import kz.bitter.ulyqbek.model.Lessons;
import kz.bitter.ulyqbek.model.Roles;
import kz.bitter.ulyqbek.model.Users;
import kz.bitter.ulyqbek.service.CourseService;
import kz.bitter.ulyqbek.service.GroupService;
import kz.bitter.ulyqbek.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
  @Autowired
  private UserService userService;

  @Autowired
  private CourseService courseService;

  @Autowired
  private GroupService groupService;

  @GetMapping(value = "/user-panel")
  public String userPanel(Model model) {
    model.addAttribute("allUsers", userService.getAllUsers());
    model.addAttribute("allRoles", userService.getAllRoles());
    model.addAttribute("currentUser", getUserData());
    return "admin/panel";
  }

  @GetMapping(value = "/group-panel")
  public String groupPanel(Model model) {
    model.addAttribute("allGroups", groupService.getAllGroups());
    model.addAttribute("currentUser", getUserData());
    return "group/panel";
  }

  @PostMapping(value = "/remove-user")
  public String removeUser(
      @RequestParam(name = "user_id") Long id) {
    Users user = userService.getUserById(id);
    if (user != null) {
      userService.removeUserById(id);
      return "redirect:/admin/user-panel?removeSuccess=" + id;
    }
    return "redirect:/admin/user-panel?removeError=true";
  }

  @PostMapping(value = "/save-account")
  public String saveUserAccount(@RequestParam(name = "user_id") String id,
      @RequestParam(name = "user_email") String userEmail,
      @RequestParam(name = "user_nickname") String userNickname,
      @RequestParam(name = "user_password") String password) {
    Users user = userService.getUserById(Long.parseLong(id));
    if (user != null) {
      user.setEmail(userEmail);
      user.setUsername(userNickname);
      return userService.saveUser(user) != null ? "redirect:/admin/user-panel?saveSuccess=" + id
          : "redirect:/admin/user-panel?saveError=true";
    }
    return "redirect:/";
  }

  @PostMapping(value = "/save-user-role/{id}")
  public String saveUserRole(@PathVariable("id") Long id,
      @RequestParam("roles") Long[] roles) {
    Users user = userService.getUserById(id);
    List<Roles> userRoles = new ArrayList<>();
    for (Long i : roles) {
      userRoles.add(userService.getRole(i));
    }
    user.setRoles(userRoles);
    userService.saveUser(user);
    return "redirect:/admin/user-panel";
  }

  @PostMapping(value = "/save-group")
  public String saveGroup(
      @RequestParam(name = "group_id") Long id,
      @RequestParam(name = "group_name") String name,
      @RequestParam(name = "group_description") String description) {
    Groups group = new Groups();
    if (id != -1) {
      group.setId(id);
    }
    group.setName(name);
    group.setDescription(description);

    groupService.saveGroups(group);
    return "redirect:/admin/group-panel";
  }

  @PostMapping(value = "/save-user-to-group")
  public String saveUserToGroup(
      @RequestParam(name = "user_id") Long userId,
      @RequestParam(name = "group_id") Long groupId) {
    Users user = userService.getUserById(userId);
    Groups groups = groupService.getGroupById(groupId);
    userService.saveUserToGroup(user, groups);
    return "redirect:/admin/edit/group/" + groupId;
  }

  @PostMapping(value = "/signUpFull")
  public String toSignUpFull(@RequestParam(name = "user_email") String email,
      @RequestParam(name = "user_nickname") String nickname,
      @RequestParam(name = "user_password") String password,
      @RequestParam(name = "user_re_password") String rePassword,
      @RequestParam(name = "first_name") String firstName,
      @RequestParam(name = "last_name") String lastName) {
    Users newUser = new Users();

    newUser.setEmail(email);
    newUser.setUsername(nickname);
    newUser.setPassword(password);
    newUser.setFullname(firstName + lastName);
    newUser.setId(null);
    newUser.setPfp(null);

    if (password.equals(rePassword)) {
      newUser = userService.registerUser(newUser);
      if (newUser != null) {
        return "redirect:/admin/user-panel?regSuccess=" + newUser.getId();
      } else
        return "redirect:/admin/user-panel?regNotFree=true";
    }
    return "redirect:/admin/user-panel?regFail=true";
  }

  // @PostMapping(value = "/save-group-to-event")
  // public String saveGroupToEvent(
  // @RequestParam(name = "event_id") Long eventId,
  // @RequestParam(name = "group_id") Long groupId) {
  // Events event = eventService.getEventById(eventId);
  // Groups group = groupService.getGroupById(groupId);
  // eventService.saveGroupToEvent(group, event);
  // return "redirect:admin/edit/event/" + eventId;
  // }
  //
  @PostMapping(value = "/save-course-to-group")
  public String saveCourseToGroup(
      @RequestParam(name = "course_id") Long courseId,
      @RequestParam(name = "group_id") Long groupId) {
    Courses courses = courseService.getCourseById(courseId);
    Groups groups = groupService.getGroupById(groupId);
    groupService.saveCourseToGroup(groups, courses);
    return "redirect:/admin/edit/group/" + groupId;
  }

  @GetMapping(value = "/course-panel")
  public String coursePanel(Model model) {
    model.addAttribute("allCourses", courseService.getAllCourses());
    model.addAttribute("currentUser", getUserData());

    return "teacher/course/list";
  }

  @GetMapping(value = "/edit/course/{id}")
  public String editCourse(@PathVariable("id") Long id,
      Model model) {
    model.addAttribute("chapterList", courseService.getChapterByCourseId(id));
    model.addAttribute("course", courseService.getCourseById(id));
    model.addAttribute("currentUser", getUserData());
    return "teacher/course/edit";
  }

  @GetMapping(value = "/edit/chapter/{id}")
  public String editChapter(@PathVariable("id") Long id,
      Model model) {
    model.addAttribute("lessonList", courseService.getLessonsByChapterId(id));
    model.addAttribute("chapter", courseService.getChapterById(id));
    model.addAttribute("currentUser", getUserData());
    return "teacher/chapter/edit";
  }

  @GetMapping(value = "/edit/group/{id}")
  public String editGroup(@PathVariable("id") Long id,
      Model model) {
    model.addAttribute("userList", userService.getAllUsersByGroupId(id));
    model.addAttribute("courseList", groupService.getGroupById(id).getCourses());
    model.addAttribute("group", groupService.getGroupById(id));
    model.addAttribute("currentUser", getUserData());
    return "group/edit";
  }

  @GetMapping(value = "/edit/lesson/{id}")
  public String editLesson(Model model,
      @PathVariable("id") Long id) {

    Lessons lesson = courseService.getLessonbyId(id);
    model.addAttribute("currentLesson", lesson);
    model.addAttribute("currentUser", getUserData());
    return "teacher/lesson/edit";
  }

  @PostMapping(value = "/save-course")
  public String saveCourse(
      @RequestParam(name = "course_id") Long id,
      @RequestParam(name = "course_name") String name,
      @RequestParam(name = "course_description") String description,
      @RequestParam(name = "course_reputation") int reputation,
      @RequestParam(name = "course_price") int price) {
    Courses courses = new Courses();
    if (id != -1) {
      courses.setId(id);
    }
    courses.setName(name);
    courses.setDescription(description);
    courses.setReputation(reputation);
    courses.setPrice(price);

    courseService.saveCourse(courses);
    return "redirect:/admin/course-panel";
  }

  @PostMapping(value = "/save-chapter")
  public String saveChapter(
      @RequestParam(name = "course_id") Long courseId,
      @RequestParam(name = "course_name") String name,
      @RequestParam(name = "course_description") String description,
      @RequestParam(name = "chapter_id") Long id) {
    Chapters chapter = new Chapters();
    if (id != -1) {
      chapter.setId(id);
    }
    Courses course = courseService.getCourseById(courseId);
    chapter.setCourse(course);
    chapter.setName(name);
    chapter.setDescription(description);

    courseService.saveChapter(chapter);
    return "redirect:/admin/edit/course/" + courseId;
  }

  @PostMapping(value = "/save-lesson")
  public String saveLesson(Lessons lesson,
      Model model,
      @RequestParam(name = "chapter_id") Long chapterId) {
    lesson.setChapter(courseService.getChapterById(chapterId));
    courseService.saveLesson(lesson);
    return "redirect:/admin/edit/chapter/" + lesson.getChapter().getId();
  }

  // @PostMapping(value = "/save-account")
  // public String saveUserAccount(@RequestParam(name = "user_id") String id,
  // @RequestParam(name = "user_email") String userEmail,
  // @RequestParam(name = "user_nickname") String userNickname,
  // @RequestParam(name = "user_password") String password) {
  // Users user = userService.getUserById(Long.parseLong(id));
  // if (user != null) {
  // user.setEmail(userEmail);
  // user.setUsername(userNickname);
  // return userService.saveUser(user) != null ?
  // "redirect:admin/user-panel?saveSuccess=" + id
  // : "redirect:/admin/user-panel?saveError=true";
  // }
  // return "redirect:/profile";
  // }

  @PostMapping(value = "/new-lesson")
  public String saveLesson(@RequestParam(name = "chapter_id") Long chapterId) {
    Lessons lesson = new Lessons();
    lesson.setChapter(courseService.getChapterById(chapterId));
    lesson = courseService.saveLesson(lesson);
    return "redirect:/admin/edit/lesson/" + lesson.getId();
  }

  @PostMapping(value = "/remove-course")
  public String removeCourse(
      @RequestParam(name = "course_id") Long id) {
    Courses course = courseService.getCourseById(id);
    if (course != null) {
      courseService.removeCourse(id);
      return "redirect:/admin/course-panel?removeSuccess=" + id;
    } else {
      return "redirect:/profile";
    }
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
