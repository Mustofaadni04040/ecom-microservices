package com.ecommerce.notification;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import com.ecommerce.notification.payload.OrderStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.name}")

    public void handleOrderEvent(OrderCreatedEvent event) {
        System.out.println("Received order event: " + event);

        long orderId = event.getOrderId();
        OrderStatus status = event.getStatus();

        System.out.println("Oder ID: " + orderId);
        System.out.println("Oder Status: " + status);
    }
}
