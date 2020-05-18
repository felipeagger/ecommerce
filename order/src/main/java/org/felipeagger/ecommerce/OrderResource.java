package org.felipeagger.ecommerce;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.felipeagger.ecommerce.controller.OrderController;
import org.felipeagger.ecommerce.entity.Order;
import org.felipeagger.ecommerce.entity.OrderDetail;
import org.felipeagger.ecommerce.entity.OrderItems;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/orders")
@Consumes(MediaType.TEXT_HTML)                         
@Produces(MediaType.TEXT_HTML)
@Transactional
public class OrderResource {

    @Inject Template order;
    @Inject Template orderDetail;
    @Inject Template redirect;

    @Inject 
    private OrderController orderController;

    @GET
    @Path("/")
    public TemplateInstance getOrders(@HeaderParam("Cookie") String cookie) {
        
        if(!orderController.validateAuth(cookie)){
            return redirect.data("data", null);
        }
        
        String userId = orderController.getUserAuth(cookie);

        if(orderController.validateUuid(userId)){

            List<Order> userOrders = Order.list("user_id", userId);

            return order.data("data", userOrders);
        }

        return order.data("data", null);

    }

    @GET
    @Path("/{id}")
    public TemplateInstance getOrderDetail(@HeaderParam("Cookie") String cookie, @PathParam("id") String id) {
        
        if(!orderController.validateAuth(cookie)){
            return redirect.data("data", null);
        }
        
        String userId = orderController.getUserAuth(cookie);

        if(orderController.validateUuid(userId)){

            Order order = Order.findById(id);

            List<OrderItems> items = OrderItems.list("order_id", id);

            items = orderController.getItemsData(items);

            OrderDetail order_detail = new OrderDetail();

            order_detail.order = order;
            order_detail.items = items;

            return orderDetail.data("data", order_detail);
        }

        return orderDetail.data("data", null);

    }
}