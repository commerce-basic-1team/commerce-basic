package _team.commerce.domain.order.controller.service;

import _team.commerce.domain.order.controller.dto.OrderDetailResponse;
import _team.commerce.domain.member.entity.Member;
import _team.commerce.domain.member.repository.MemberRepository;
import _team.commerce.domain.order.controller.dto.OrderCancelRequest;
import _team.commerce.domain.order.controller.dto.OrderCreateRequest;
import _team.commerce.domain.order.controller.dto.OrderCreateResponse;
import _team.commerce.domain.order.controller.repository.OrderRepository;
import _team.commerce.domain.order.entity.Order;
import _team.commerce.domain.order.entity.OrderItem;
import _team.commerce.domain.payment.entity.Payment;
import _team.commerce.domain.payment.repository.PaymentRepository;
import _team.commerce.domain.product.entity.Product;
import _team.commerce.domain.product.repository.ProductRepository;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;


    @Transactional
    public OrderCreateResponse createOrder(
            Long memberId,
            OrderCreateRequest request
    ) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.MEMBER_NOT_FOUND)
                );


        List<OrderItem> orderItems = new ArrayList<>();

        Long totalAmount = 0L;


        for (OrderCreateRequest.OrderItemRequest itemRequest
                : request.items()) {

            Product product = productRepository
                    .findById(itemRequest.productId())
                    .orElseThrow(() ->
                            new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
                    );


            product.decreaseStock(itemRequest.quantity());

            OrderItem orderItem = OrderItem.create(
                    product,
                    itemRequest.quantity(),
                    product.getPrice()
            );


            orderItems.add(orderItem);


            Long itemTotalAmount =
                    product.getPrice() * itemRequest.quantity();


            totalAmount += itemTotalAmount;
        }


        String orderNumber = UUID.randomUUID().toString();


        Order order = Order.create(
                member,
                orderNumber,
                totalAmount
        );


        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }


        Order savedOrder = orderRepository.save(order);


        paymentRepository.save(
                Payment.createPending(savedOrder)
        );


        return new OrderCreateResponse(
                savedOrder.getId(),
                savedOrder.getOrderNumber(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus()
        );
    }
    @Transactional
    public void cancelOrder(
            Long memberId,
            Long orderId,
            OrderCancelRequest request
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.ORDER_NOT_FOUND)
                );

        if (!order.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.PAYMENT_NOT_FOUND)
                );

        if (!payment.isPending()) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS);
        }

        order.cancel(request.reason());

        payment.fail();

        for (OrderItem orderItem : order.getOrderItems()) {

            Product product = orderItem.getProduct();

            product.increaseStock(orderItem.getQuantity());
        }
    }


    public OrderDetailResponse getOrderDetail(
            Long memberId,
            Long orderId
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.ORDER_NOT_FOUND)
                );

        if (!order.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }


        return OrderDetailResponse.from(order);
    }
}

