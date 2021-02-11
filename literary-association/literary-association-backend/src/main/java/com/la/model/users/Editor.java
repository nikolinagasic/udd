package com.la.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("EDITOR")
public class Editor extends SysUser implements Serializable {

    public Editor() {
    }

    public Editor(String username, String password, String firstName, String lastName, String state, String city, String email) {
        super(username, password, firstName, lastName, state, city, email);
    }
}
