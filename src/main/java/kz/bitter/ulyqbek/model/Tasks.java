package kz.bitter.ulyqbek.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Tasks extends BaseEntity{
    @Column (name = "order_place")
    private Long orderPlace;

    @ManyToOne (fetch = FetchType.LAZY )
    private Chapters chapter;

    @Column (name = "name")
    private String name;

    public abstract String returnClass ();
}
