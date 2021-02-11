package com.la.model.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@DiscriminatorValue("BOARD_MEMBER")
public class BoardMember extends SysUser implements Serializable {

    public BoardMember() {
    }
}
