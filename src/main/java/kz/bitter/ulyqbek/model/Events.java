package kz.bitter.ulyqbek.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "t_events")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Events extends BaseEntity {
  @Column
  private String name;

  @Column
  private Date date;

  @ManyToOne()
  Courses courses;

  @ManyToMany
  List<Groups> groups;
}
