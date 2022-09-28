// package kz.bitter.ulyqbek.repository;
//
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;
//
// import kz.bitter.ulyqbek.model.Tasks;
//
// import javax.transaction.Transactional;
// import java.util.List;
//
// @Transactional
// @Repository
// public interface TaskRepository<T extends Tasks> extends JpaRepository <T,Long> {
//     List<Tasks> findByChapterId (Long id);
//     void deleteAllByChapterCourseId (Long id);
//     void deleteAllByChapterId (Long id);
// }
