package _team.commerce.domain.order.repository;

import _team.commerce.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByMemberId(
            Long memberId,
            Pageable pageable
    );
}