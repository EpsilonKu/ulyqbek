package kz.bitter.ulyqbek.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_position")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastPosition extends BaseEntity {
  @ManyToOne
  private Courses courses;

  // @ManyToOne
  // private Chapters chapters;

  @ManyToOne
  private Lessons lessons;

  @ManyToOne
  private Users users;
}
