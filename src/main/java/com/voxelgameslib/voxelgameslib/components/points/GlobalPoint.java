package com.voxelgameslib.voxelgameslib.components.points;

import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@Table(name = "point_definition")
public class GlobalPoint implements Point {
    private String name;
    private boolean persist;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isPersist() {
        return persist;
    }

    @Override
    public void setPersist(boolean persist) {
        this.persist = persist;
    }
}
