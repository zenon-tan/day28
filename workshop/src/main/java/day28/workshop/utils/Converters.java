package day28.workshop.utils;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Converters {

    public static JsonObject gameConverter(Document doc, Document reviews, Double avg) {

        JsonObjectBuilder builder = Json.createObjectBuilder()
        .add("game_id", doc.getInteger("gid"))
        .add("name", doc.getString("name"))
        .add("year", doc.getInteger("year"))
        .add("rank", doc.getInteger("ranking"))
        .add("average", avg)
        .add("users_rated", doc.getInteger("users_rated"))
        .add("url", doc.getString("url"))
        .add("thumbnail", doc.getString("image"))
        .add("reviews", reviewsConverter(reviews))
        .add("timestamp", LocalDateTime.now().toString());

        return builder.build();
        
    }

    public static JsonArrayBuilder reviewsConverter(Document doc) {

        List<String> reviews = doc.getList("reviews", String.class);
        JsonArrayBuilder builder = Json.createArrayBuilder();
        
        for(String i : reviews) {
            builder.add(i);
        }

        return builder;

    }

    public static JsonObject listConverter(List<Document> docs, Boolean byOrder) {
        
        String order = "";
        if(byOrder) {
            order = "highest";
        }else if(!byOrder) {
            order = "lowest";
        }

        JsonObjectBuilder json = Json.createObjectBuilder()
        .add("rating", order);

        JsonArrayBuilder arr = Json.createArrayBuilder();
        
        for(Document d : docs) {

            JsonObjectBuilder game = Json.createObjectBuilder().add("_id", d.getInteger("gid"))
            .add("name", d.getString("name"))
            .add("rating", d.getInteger("rating"))
            .add("user", d.getString("user"))
            .add("comment", d.getString("c_text"))
            .add("review_id", d.getString("c_id"));

            arr.add(game);
        }

        json.add("games", arr);
        json.add("timestamp", LocalDateTime.now().toString());

        return json.build();
    }

    
}
