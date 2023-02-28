package day28.mock.configs.util;

import org.bson.Document;

import day28.mock.models.Order;

public class ToBson {

    public static Document orderToDocument(Order order) {

        Document doc = new Document();
        doc.append("name", order.getName())
        .append("email", order.getEmail())
        .append("delivery_date", order.getDeliveryDate())
        .append("line_items", order.getLineItems());

        return doc;


    }
    
}
