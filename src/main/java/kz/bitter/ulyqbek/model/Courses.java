package kz.bitter.ulyqbek.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table (name = "t_courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courses extends BaseEntity{

    @Column (name = "name")
    private String name;

    @Column (name = "description")
    private String description;

	@Column (name = "price")
	private int price;

	@Column (name = "reputation")
	private int reputation;

}
