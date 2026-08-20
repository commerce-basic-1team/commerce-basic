package _team.commerce.domain.cart.repository;

import _team.commerce.domain.auth.entity.Member;
import _team.commerce.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMember(Member member);
}