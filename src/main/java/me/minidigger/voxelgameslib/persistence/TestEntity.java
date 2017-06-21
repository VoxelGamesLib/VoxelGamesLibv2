package me.minidigger.voxelgameslib.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class TestEntity {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String test;

    public TestEntity(String test) {
        this.test = test;
    }

    public TestEntity(){
        //jpa
    }
}
