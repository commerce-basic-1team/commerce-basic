package _team.commerce.domain.cart.repository;

import _team.commerce.domain.cart.entity.Cart;
import _team.commerce.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCart(Cart cart);
}