package com.myself.dao.bean;

import com.myself.dao.anno.Column;
import com.myself.dao.anno.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("filter")
class Filter {

    @Column("id")
    private int id;

    @Column("user_name")
    private String username;

    @Column("pass_word")
    private String password;

    @Column("age")
    private int age;

    @Column("city")
    private String city;

    @Column("email")
    private String email;

    @Column("mobile")
    private String mobile;

}
