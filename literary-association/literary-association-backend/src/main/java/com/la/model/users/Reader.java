package com.la.model.users;

import com.la.model.Genre;
import com.la.model.Membership;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("READER")
public class Reader extends SysUser implements Serializable {

    @Column
    private boolean beta;

    @Column
    private Integer penaltyPoints;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "beta_reader_genre",
            joinColumns = @JoinColumn(name = "beta_reader_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> betaReaderGenres;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private Membership membership;
}

