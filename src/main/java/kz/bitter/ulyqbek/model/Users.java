package kz.bitter.ulyqbek.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String fullname;

    @Column (name = "email")
    private String email;

    @Column (name = "username")
    private String username;

    @Column (name = "password")
    private String password;

    @Column (name  = "pfp")
    private String pfp;

    @ManyToMany (fetch = FetchType.LAZY)
    private List <Groups> groups;

    @ManyToMany (fetch = FetchType.LAZY)
    private List<Courses> courses;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Roles> roles = new ArrayList<>();
}
