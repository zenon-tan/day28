package day28.mock.repo;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static day28.mock.configs.Constants.*;

import day28.mock.configs.util.ToBson;
import day28.mock.models.Order;

@Repository
public class OrdersRepo {

    @Autowired
    MongoTemplate mongoTemplate;

    public String insertOrder(Order order) {

        Document doc = ToBson.orderToDocument(order);

        Document result = mongoTemplate.insert(doc, COLLECTIONS_ORDER);
        ObjectId id = result.getObjectId("_id");

        return id.toString();
        
    }
    
}
