package day28.workshop.utils;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

public class CustomAggregationOps implements AggregationOperation {

    private String jsonOperation;

    public CustomAggregationOps(String jsonOperation) {
        this.jsonOperation = jsonOperation;
    }

    @Override
    public Document toDocument(AggregationOperationContext context) {

        return context.getMappedObject(org.bson.Document.parse(jsonOperation));

    }


    
}
