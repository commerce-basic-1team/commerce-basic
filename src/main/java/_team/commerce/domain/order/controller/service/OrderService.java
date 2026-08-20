package _team.commerce.domain.order.controller.service;

import _team.commerce.domain.auth.entity.Member;
import _team.commerce.domain.auth.repository.MemberRepository;
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

    // 주문 생성 후 PENDING 상태의 결제를 저장하기 위한 Repository
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
                Payment.createPending(savedOrder.getId())
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

        // 1. 취소하려는 주문 찾기
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.ORDER_NOT_FOUND)
                );


        // 2. 현재 로그인한 회원의 주문인지 확인
        // 다른 사람의 주문을 취소하지 못하도록 검사
        if (!order.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }


        // 3. 주문 상태를 취소 상태로 변경하고 취소 사유 저장
        //
        // 이미 취소된 주문이면
        // Order.cancel() 내부에서 ALREADY_CANCELED 예외 발생
        order.cancel(request.reason());


        // 4. 주문했던 상품들의 재고 복구
        for (OrderItem orderItem : order.getOrderItems()) {

            // 주문 상품에서 실제 상품 가져오기
            Product product = orderItem.getProduct();

            // 주문했던 수량만큼 재고 증가
            product.increaseStock(orderItem.getQuantity());
        }


        // 별도로 save()를 호출하지 않아도 됨.
        //
        // @Transactional 안에서 조회한 Order와 Product는
        // JPA가 변경 사항을 감지해서 트랜잭션이 끝날 때 DB에 반영한다.
    }
}