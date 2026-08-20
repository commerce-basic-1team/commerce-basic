package _team.commerce.domain.order.controller;

import _team.commerce.domain.order.controller.dto.OrderCreateRequest;
import _team.commerce.domain.order.controller.dto.OrderCreateResponse;
import _team.commerce.domain.order.controller.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreateResponse createOrder(
            @RequestParam Long memberId,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        return orderService.createOrder(memberId, request);
    }
}