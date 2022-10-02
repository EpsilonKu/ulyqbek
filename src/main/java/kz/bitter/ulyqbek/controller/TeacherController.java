package kz.bitter.ulyqbek.controller;

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
import kz.bitter.ulyqbek.model.Lessons;
import kz.bitter.ulyqbek.model.Users;
import kz.bitter.ulyqbek.service.CourseService;
import kz.bitter.ulyqbek.service.UserService;

@Controller
@RequestMapping("/admin")
public class TeacherController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

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

    @PostMapping(value = "/save-account")
    public String saveUserAccount(@RequestParam(name = "user_id") String id,
            @RequestParam(name = "user_email") String userEmail,
            @RequestParam(name = "user_nickname") String userNickname,
            @RequestParam(name = "user_password") String password) {
        Users user = userService.getUserById(Long.parseLong(id));
        if (user != null) {
            user.setEmail(userEmail);
            user.setUsername(userNickname);
            return userService.saveUser(user) != null ? "redirect:admin/user-panel?saveSuccess=" + id
                    : "redirect:/admin/user-panel?saveError=true";
        }
        return "redirect:/profile";
    }

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
