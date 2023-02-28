package day28.workshop.services;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import day28.workshop.repositories.GameRepo;
import day28.workshop.utils.Converters;
import jakarta.json.JsonObject;

@Service
public class GameService {

    @Autowired
    GameRepo gRepo;

    public Optional<Document> checkGameByGid(int gid) {

        Optional<Document> result = gRepo.getGameByGid(gid);
        if(result.isPresent()) {
            return Optional.of(result.get());
        }

        return Optional.empty();
        

    }

    public JsonObject getGameReviewsByGid(int gid) {

        Document reviews = gRepo.getReviewsByGid(gid);
        Document game = gRepo.getGameByGid(gid).get();
        Double avg = gRepo.getAvgByGid(gid);

        JsonObject json = Converters.gameConverter(game, reviews, avg);

        return json;

    }

    public JsonObject getGameReviewsByOrder(String order) {
        
        Boolean byOrder = true;

        if(order.equalsIgnoreCase("highest")) {

            byOrder = true;

        } else if(order.equalsIgnoreCase("lowest")) {
            byOrder = false;
        }
        List<Document> results = gRepo.getGamesBySortedRatings(byOrder);

        JsonObject json = Converters.listConverter(results, byOrder);

        return json;
    }
    
}
