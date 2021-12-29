package com.max.movierating.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
public class Country extends BaseEntity {

    public Country() {
    }

    public Country(String name) {
        this.name = name;
    }

    @Column(unique = true, length = 25)
    private String name;

}
