package model;

import lombok.Data;

@Data
public class Room extends Element {
    private String name;

    public Room(String name) {
        this.name = name;
    }
}