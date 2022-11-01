package kz.bitter.ulyqbek.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

  @Value("${file.avatar.uploadPath}")
  private String uploadPath;

  @Value("${file.avatar.linkPath}")
  private String linkPath;

  @Value("${file.avatar.defaultAvaPath}")
  private String defaultAvaPath;

  @GetMapping(value = "/")
  @PreAuthorize("isAnonymous()")
  public String index(Model model) {
    // model.addAttribute("gender", "");
    // model.addAttribute("currentUser", getUserDaa());
    return "guest/index";
  }

  @GetMapping(value = "/register")
  @PreAuthorize("isAnonymous()")
  public String register(Model model) {

    // model.addAttribute("gender", "");
    // model.addAttribute("currentUser", getUserDaa());
    return "guest/register";
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

  @PostMapping(value = "/save-user-settings")
  public String saveUserAccount(@RequestParam(name = "user_id") String id,
      @RequestParam(name = "user_email") String userEmail,
      @RequestParam(name = "user_nickname") String userNickname,
      @RequestParam(name = "user_password") String password) {
    Users user = userService.getUserById(Long.parseLong(id));
    if (user != null) {
      user.setEmail(userEmail);
      user.setUsername(userNickname);
      userService.saveUser(user);
      return "redirect:/setting";
    }
    return "redirect:/";
  }

  @PostMapping(value = "/save-user-password")
  public String saveUserPassword(@RequestParam(name = "user_id") String id,
      @RequestParam(name = "user_new_password") String newPassword,
      @RequestParam(name = "user_re_new_password") String reNewPassword,
      @RequestParam(name = "user_old_password") String password) {
    Users user = userService.getUserById(Long.parseLong(id));
    if (user != null) {
      userService.saveUserPassword(user, newPassword, password);
      return "redirect:/setting";
    }
    return "redirect:/setting";
  }

  @GetMapping(value = "/viewPhoto/{url}", produces = { MediaType.IMAGE_JPEG_VALUE })
  @PreAuthorize("isAuthenticated()")
  public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException {
    InputStream in;
    try {

      ClassPathResource resource = new ClassPathResource(linkPath + url + ".jpg");
      in = resource.getInputStream();
    } catch (Exception e) {
      ClassPathResource resource = new ClassPathResource(defaultAvaPath);
      in = resource.getInputStream();
    }
    return IOUtils.toByteArray(in);
  }

  @PostMapping(value = "/uploadPfp")
  public String uploadAva(@RequestParam(name = "photo") MultipartFile file) {

    Users user = getUserData();

    if (file.getContentType() != null && user != null
        && (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png"))) {
      try {

        String filenam = "pfp_" + DigestUtils.sha1Hex("pfp_" + user.getId());

        // byte[] bytes = ImageUtils.cropImageSquare(file.getBytes());
        byte[] bytes = file.getBytes();

        Path path = Paths.get(uploadPath + filename + ".jpg");
        Files.write(path, bytes);

        user.setPfp(filename);
        userService.saveUser(user);

        return "redirect:/setting";
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error on uploadPfP");
      }
    }
    return "redirect:/profile";
  }

  @GetMapping(value = "/setting")
  @PreAuthorize("isAuthenticated()")
  public String setting(Model model) {
    model.addAttribute("currentUser", getUserData());
    return "user/setting";
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
