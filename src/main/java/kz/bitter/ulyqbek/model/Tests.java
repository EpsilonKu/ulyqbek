package kz.bitter.ulyqbek.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Tests extends Tasks {

    @Column (name = "content", columnDefinition = "TEXT")
    private String content;

    @Column (name = "html_content", columnDefinition = "TEXT")
    private String htmlContent;

}
