package com.la.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
public class Administrator extends SysUser implements Serializable {

    public Administrator() {
    }

    public Administrator(String username, String password, String firstName, String lastName, String state, String city, String email) {
        super(username, password, firstName, lastName, state, city, email);
    }
}
