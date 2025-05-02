package com.example.evostyle.domain.cart.service;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.CartItemResponse;
import com.example.evostyle.domain.cart.dto.response.CartResponse;
import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestCartService {

    private final ProductDetailRepository productDetailRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public final String GUEST_CART_KEY_PREFIX = "guest_cart::";

    public void addCartItemGuest(AddCartItemRequest request, String cartToken, Long productDetailId) {

        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        boolean existKey = redisTemplate.hasKey(GUEST_CART_KEY_PREFIX + cartToken);

        if (redisTemplate.opsForHash().hasKey(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetail.getId()))) {
            throw new ConflictException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        }

        int price = productDetail.getProduct().getPrice() * request.quantity();

        RedisCartItemDto redisCartItem = RedisCartItemDto.of(productDetailId, request.quantity(), price);
        redisTemplate.opsForHash().put(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetail.getId()), redisCartItem);

        if (!existKey) {
            redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofDays(1));
        }
    }


    public CartResponse readCartGuest(String cartToken) {
        boolean existKey = redisTemplate.hasKey(GUEST_CART_KEY_PREFIX + cartToken);

        List<RedisCartItemDto> list = redisTemplate.opsForHash().entries(GUEST_CART_KEY_PREFIX + cartToken)
                .values().stream().map(o -> (RedisCartItemDto) o).toList();

        if (!existKey) {
            redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofDays(1));
        }

        List<CartItemResponse> cartItemResponseList = list.stream().map(CartItemResponse::from).toList();
        return CartResponse.of(cartItemResponseList);
    }


    public void updateCartItemQuantity(UpdateCartItemRequest request, String cartToken, Long productDetailId) {

        if (!productDetailRepository.existsById(productDetailId)) {
            throw new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND);
        }


        RedisCartItemDto redisCartItemDto =
                (RedisCartItemDto) redisTemplate.opsForHash().get(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetailId));

        if (redisCartItemDto == null) {
            throw new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        redisCartItemDto.updateQuantity(request.quantity());
        redisTemplate.opsForHash().put(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetailId), redisCartItemDto);

    }


    public void deleteCartItem(Long productDetailId, String cartToken) {
        boolean existKey = redisTemplate.hasKey(GUEST_CART_KEY_PREFIX + cartToken);

        if (!redisTemplate.opsForHash().hasKey(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetailId))) {
            throw new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        redisTemplate.opsForHash().delete(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetailId));
        if (!existKey) {
            redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofDays(1));
        }
    }

    public CartResponse emptyCart(String cartToken) {
        redisTemplate.delete(GUEST_CART_KEY_PREFIX + cartToken);
        return CartResponse.of(new ArrayList<>());
    }

}
