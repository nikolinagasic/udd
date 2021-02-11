package com.la.model.users;

import com.la.model.Membership;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@DiscriminatorValue("WRITER")
public class Writer extends SysUser implements Serializable {

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private Membership membership;

    public Writer() {
    }

    public Writer(String username, String password, String firstName, String lastName, String state, String city, String email, Membership membership) {
        super(username, password, firstName, lastName, state, city, email);
        this.membership = membership;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
