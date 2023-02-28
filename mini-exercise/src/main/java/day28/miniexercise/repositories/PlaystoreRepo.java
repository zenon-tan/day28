package day28.miniexercise.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;

import static day28.miniexercise.config.Constants.*;

@Repository
public class PlaystoreRepo {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Document> groupAppByCategory() {

        String notNaNExp = """
            "Rating": {$ne: NaN}
                """;

        MatchOperation match = Aggregation
        .match(AggregationExpression.from(MongoExpression.create(notNaNExp)));

        MatchOperation match2 = Aggregation
        .match(Criteria.where("Rating").ne(Double.NaN));

        GroupOperation group = Aggregation.group("Category")
        .push(new BasicDBObject("Name", "$App").append("Rating", "$Rating").append("Type", "$Type")).as("App")
        ;

        Aggregation pipeline = Aggregation.newAggregation(match, group);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, DB_PLAYSTORE, Document.class);

        return results.getMappedResults();
    }
    
}
