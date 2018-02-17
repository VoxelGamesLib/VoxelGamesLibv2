package com.voxelgameslib.voxelgameslib.components.points;

import javax.persistence.Table;

//@Entity
@Table(name = "point_definition")
public class GlobalPoint implements Point {

    private String name;
    private boolean persist;

    @java.beans.ConstructorProperties({"name", "persist"})
    public GlobalPoint(String name, boolean persist) {
        this.name = name;
        this.persist = persist;
    }

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
