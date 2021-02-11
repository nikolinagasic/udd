package com.la.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {
}
