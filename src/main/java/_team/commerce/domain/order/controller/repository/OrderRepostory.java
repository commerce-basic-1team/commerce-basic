package _team.commerce.domain.order.controller.repository;

import _team.commerce.domain.order.controller.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepostory extends JpaRepository<Order, Long> {

}
