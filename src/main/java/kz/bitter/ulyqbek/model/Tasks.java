package kz.bitter.ulyqbek.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Tasks extends BaseEntity{
    @Column (name = "order_place")
    private Long orderPlace;

    @ManyToOne (fetch = FetchType.LAZY )
    private Chapters chapter;

    @Column (name = "name")
    private String name;
}
