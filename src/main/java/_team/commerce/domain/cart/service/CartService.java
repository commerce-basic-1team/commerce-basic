package _team.commerce.domain.cart.service;

import _team.commerce.domain.member.entity.Member;
import _team.commerce.domain.member.repository.MemberRepository;
import _team.commerce.domain.cart.dto.request.CartItemCreateRequest;
import _team.commerce.domain.cart.dto.request.CartItemQuantityUpdateRequest;
import _team.commerce.domain.cart.dto.response.CartItemResponse;
import _team.commerce.domain.cart.dto.response.CartResponse;
import _team.commerce.domain.cart.entity.Cart;
import _team.commerce.domain.cart.entity.CartItem;
import _team.commerce.domain.cart.repository.CartItemRepository;
import _team.commerce.domain.cart.repository.CartRepository;
import _team.commerce.domain.product.entity.Product;
import _team.commerce.domain.product.repository.ProductRepository;
import _team.commerce.global.exception.CustomException;
import _team.commerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 상품 담기
     */
    @Transactional
    public void addItem(Long memberId, CartItemCreateRequest request) {
        Member member = findMember(memberId);
        Product product = findProduct(request.productId());

        validateStock(product, request.quantity());

        Cart cart = getOrCreateCart(member);

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem == null) {
            cartItem = CartItem.create(cart, product, request.quantity());
        } else {
            int newQuantity = cartItem.getQuantity() + request.quantity();
            validateStock(product, newQuantity);
            cartItem.changeQuantity(newQuantity);
        }

        cartItemRepository.save(cartItem);
    }

    /**
     * 장바구니 조회
     */
    @Transactional
    public CartResponse getCart(Long memberId) {
        Member member = findMember(memberId);
        Cart cart = getOrCreateCart(member);

        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        List<CartItemResponse> items = cartItems.stream()
                .map(CartItemResponse::from)
                .toList();

        long totalAmount = cartItems.stream()
                .mapToLong(cartItem ->
                        cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();

        return new CartResponse(
                cart.getId(),
                items,
                totalAmount
        );
    }

    /**
     * 수량 변경
     */
    @Transactional
    public void updateQuantity(
            Long memberId,
            Long cartItemId,
            CartItemQuantityUpdateRequest request
    ) {
        Member member = findMember(memberId);
        Cart cart = getOrCreateCart(member);

        CartItem cartItem = findCartItem(cartItemId, cart);

        validateStock(cartItem.getProduct(), request.quantity());

        cartItem.changeQuantity(request.quantity());
    }

    /**
     * 장바구니 상품 개별 삭제
     */
    @Transactional
    public void deleteItem(Long memberId, Long cartItemId) {
        Member member = findMember(memberId);
        Cart cart = getOrCreateCart(member);

        CartItem cartItem = findCartItem(cartItemId, cart);

        cartItemRepository.delete(cartItem);
    }

    /**
     * 장바구니 전체 비우기
     */
    @Transactional
    public void clearCart(Long memberId) {
        Member member = findMember(memberId);
        Cart cart = getOrCreateCart(member);

        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        cartItemRepository.deleteAll(cartItems);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Cart getOrCreateCart(Member member) {
        return cartRepository.findByMember(member)
                .orElseGet(() -> cartRepository.save(Cart.create(member)));
    }

    /**
     * 장바구니 상품 조회 및 소유권 확인
     */
    private CartItem findCartItem(Long cartItemId, Cart cart) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        return cartItem;
    }

    private void validateStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }
    }
}