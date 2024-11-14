package ch.hslu.swda.business;

import ch.hslu.swda.entities.Order;
import ch.hslu.swda.messages.LogMessage;
import ch.hslu.swda.messages.VerifyResponse;
import ch.hslu.swda.micro.OrderService;
import ch.hslu.swda.micro.Service;

import java.io.IOException;

public class ModifyValidity implements Modifiable {
    private final VerifyResponse response;
    private final Service service;

    public ModifyValidity(VerifyResponse response, OrderService service) {
        this.response = response;
        this.service = service;
    }
    @Override
    public void modify(Order order) throws IOException, InterruptedException {
        if (response.valid()) {
            order.getState().setValid(true);
            service.log(new LogMessage(order.getEmployeeId(), "order.validate", "Order Validated: " + order.toString()));
            service.requestArticlesFromStore(order.getStoreRequest());
            service.checkCustomerValidity(order.getCustomerRequest());
        } else {
            order.getState().setCancelled(true);
            service.log(new LogMessage(order.getEmployeeId(), "order.validate", "Order not validated " + order.toString()));
        }
    }
}
