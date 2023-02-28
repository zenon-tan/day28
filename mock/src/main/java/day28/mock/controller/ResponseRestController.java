package day28.mock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import day28.mock.models.Order;
import day28.mock.models.OrderResponse;
import day28.mock.repo.OrdersRepo;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/order")
public class ResponseRestController {

    @Autowired
    OrdersRepo oRepo;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> postOrder(@Valid @RequestBody Order order, BindingResult result) {

        OrderResponse response = new OrderResponse();

        if(result.hasErrors()) {

            String error = result.getFieldErrors().toString();

            response.setOrderId("-1");
            response.setMessage(error);

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).contentType(MediaType.APPLICATION_JSON)
            .body(response);

        }

        String id = oRepo.insertOrder(order);
        System.out.println(id);

        response.setMessage("Order has been placed");
        response.setOrderId(id);

        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
        .body(response);


    }



    
}
