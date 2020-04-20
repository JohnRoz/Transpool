package model;

import java.awt.*;
import java.util.Objects;

public class Station {
    private String name;
    private Point coordinate;

    //region Ctor
    public Station(String name, int x, int y) {
        this.name = name;
        this.coordinate = new Point(x, y);
    }
    //endregion

    //region Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }
    //endregion

    //region Equals & hashCode overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        return getName().equals(station.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCoordinate());
    }
    //endregion
}
