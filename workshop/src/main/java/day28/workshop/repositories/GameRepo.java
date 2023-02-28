package day28.workshop.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.aggregation.UnsetOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.internal.operation.AggregateOperation;

import day28.workshop.utils.CustomAggregationOps;

@Repository
public class GameRepo {

    @Autowired
    MongoTemplate mongoTemplate;

    public Document getReviewsByGid(int gId) {

        MatchOperation matchGid = Aggregation.match(Criteria.where("gid").is(gId));
        LookupOperation lookupComments = Aggregation.lookup("comment", 
        "gid", "gid", "reviews");
        UnwindOperation unwindReviews = Aggregation.unwind("reviews");
        ProjectionOperation projectGid = Aggregation.project("gid").and(
            StringOperators.Concat.stringValue("/review/").concatValueOf("reviews.c_id")
        ).as("reviews.ref");
        GroupOperation groupByGid = Aggregation.group("gid").push("reviews.ref").as("reviews");

        Aggregation pipeline = Aggregation.newAggregation(matchGid, lookupComments, unwindReviews, projectGid, groupByGid);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "game", Document.class);

        return results.getMappedResults().get(0);

        
    }

    public Optional<Document> getGameByGid(int gId) {

        try {

            Document result = mongoTemplate.find(new Query(Criteria.where("gid").is(gId)), 
            Document.class, "game").get(0);
    
            return Optional.of(result);
            
        } catch (Exception e) {
            return Optional.empty();
        }


    }

    public Double getAvgByGid(int gId) {

        MatchOperation matchGid = Aggregation.match(Criteria.where("gid").is(gId));
        GroupOperation groupByGid = Aggregation.group("gid").avg("rating").as("average");

        Aggregation pipeline = Aggregation.newAggregation(matchGid, groupByGid);

        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "comment", Document.class);

        Double avg = results.getMappedResults().get(0).getDouble("average");

        return avg;
    }

    public List<Document> getGamesBySortedRatings(Boolean byOrder) {

        Direction direction = Direction.DESC;

        if(byOrder == false) {
            direction = Direction.ASC;
        }

        String customQuery = """
            {$lookup: {from: "comment", foreignField: "gid", localField: "gid",
            pipeline: [
                {$sort: {"rating": 1}},
                {$limit: 1}
            ],
            as: "review"}}
                """;
        LimitOperation limit = Aggregation.limit(1000);
        UnwindOperation unwind = Aggregation.unwind("review");
        ProjectionOperation project = Aggregation.project("gid", "name", "review.rating", "review.user", "review.c_text", "review.c_id");
        TypedAggregation<Document> customAggregation = Aggregation.newAggregation(Document.class, limit, new CustomAggregationOps(customQuery), unwind, project);


        AggregationResults<Document> results = mongoTemplate.aggregate(customAggregation, "game", Document.class);

        List<Document> docs = results.getMappedResults();



        return docs;    

    }
    
}
