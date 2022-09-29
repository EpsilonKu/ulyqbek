package kz.bitter.ulyqbek.service;

import org.springframework.data.domain.Page;

import kz.bitter.ulyqbek.model.Chapters;
import kz.bitter.ulyqbek.model.Courses;
import kz.bitter.ulyqbek.model.Lessons;
import kz.bitter.ulyqbek.model.Tasks;
import kz.bitter.ulyqbek.model.Tests;

import java.util.List;

public interface CourseService {
    List<Courses> getAllCourses ();
    void saveCourse (Courses course);
    void saveChapter (Chapters chapter);
    Lessons saveLesson (Lessons lesson);
    Tests saveTests (Tests test);

    void removeCourse (Long id);
    void removeChapter (Long id);
    void removeLesson (Long id);
    void removeTest (Long id);

    Courses getCourseById (Long id);
    Chapters getChapterById (Long id);
    Lessons getLessonbyId (Long id);
    Tests getTestbyId (Long id);

    List<Chapters> getChapterByCourseId (Long id);
    List<Lessons> getLessonsByChapterId (Long id);
    List<Tests> getTestsByChapterId (Long id);
    Page<Courses> getCoursesByPage (int page);
}
