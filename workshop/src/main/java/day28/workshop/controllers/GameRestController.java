package day28.workshop.controllers;

import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import day28.workshop.repositories.GameRepo;
import day28.workshop.services.GameService;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api")
public class GameRestController {

    @Autowired
    GameService gSrc;

    @Autowired
    GameRepo gRepo;

    @GetMapping("/{gid}/reviews")
    public ResponseEntity<String> getReviewsByGid(@PathVariable(name = "gid") int gid) {

        //Check if id exists
        Optional<Document> exists = gSrc.checkGameByGid(gid);

        if(exists.isPresent()) {

            JsonObject result = gSrc.getGameReviewsByGid(gid);
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);

        }

        return new ResponseEntity<>("Game not found", HttpStatus.NOT_FOUND);

    }

    @GetMapping("/games/{order}")
    public ResponseEntity<String> getList(@PathVariable(name = "order") String order) {

        JsonObject json = gSrc.getGameReviewsByOrder(order);
        
        return new ResponseEntity<>(json.toString(),HttpStatus.OK);
    }
    
}
