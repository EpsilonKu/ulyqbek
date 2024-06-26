package kz.bitter.ulyqbek.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roles extends BaseEntity implements GrantedAuthority {

  @Column
  private String name;

  @Override
  public String getAuthority() {
    return this.getName();
  }
}
