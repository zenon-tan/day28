package day28.workshop.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private String gameId;
    private String name;
    private int year;
    private int rank;
    private Double average;
    private int usersRated;
    private String url;
    private String thumbnail;
    
    private List<String> reviews;
    
}
