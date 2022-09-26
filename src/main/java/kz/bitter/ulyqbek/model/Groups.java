package kz.bitter.ulyqbek.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name = "t_groups")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Groups extends BaseEntity {
    @Column (name = "name")
    String name;

    @Column (name = "description")
    String description;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Courses> courses;

}
