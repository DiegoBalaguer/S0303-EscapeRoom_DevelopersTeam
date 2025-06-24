package model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString(exclude = "password")
public class Player {
    private int id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String zipCode;
    private String IdCity;
    private boolean subscribed;
    private List<Certificate> certificates;
    private List<Reward> rewards;

    public Player(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = "default";
        this.subscribed = false;
    }

}
