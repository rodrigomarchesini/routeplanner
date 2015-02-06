package com.walmart.routeplanner.domain.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Representation of a map point.
 *
 * @author Rodrigo Marchesini
 */
@NodeEntity
@TypeAlias("Point")
public class Point {

    @GraphId
    private Long id;

    private String name;

    @Indexed
    private String mapName;

    public Point() {
    }

    /**
     * @param name
     *            Point's name
     * @param mapName
     *            Map's name
     */
    public Point(String name, String mapName) {
        this.name = name;
        this.mapName = mapName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMapName() {
        return mapName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("mapName", mapName)
                .toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
