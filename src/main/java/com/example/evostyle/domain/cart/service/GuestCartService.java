package com.example.evostyle.domain.cart.service;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.GuestCartItemResponse;
import com.example.evostyle.domain.cart.dto.response.GuestCartResponse;
import com.example.evostyle.domain.cart.dto.response.MemberCartItemResponse;
import com.example.evostyle.domain.cart.dto.response.MemberCartResponse;
import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.dto.response.ProductResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.optiongroup.dto.response.OptionResponse;
import com.example.evostyle.domain.product.optiongroup.entity.Option;
import com.example.evostyle.domain.product.optiongroup.repository.OptionRepository;
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
    private final OptionRepository optionRepository;

    public final String GUEST_CART_KEY_PREFIX = "guest_cart::";


    public GuestCartItemResponse addCartItemGuest(AddCartItemRequest request, String cartToken) {

        ProductDetail productDetail = productDetailRepository.findById(request.productDetailId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        boolean existKey = redisTemplate.hasKey(GUEST_CART_KEY_PREFIX + cartToken);

        if (redisTemplate.opsForHash().hasKey(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetail.getId()))) {
            throw new ConflictException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        }

        RedisCartItemDto redisCartItem = RedisCartItemDto.of(request.productDetailId(), request.quantity());
        redisTemplate.opsForHash().put(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetail.getId()), redisCartItem);

        if (!existKey) {
            redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofDays(1));
        }

        List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(productDetail.getId())
                .stream().map(OptionResponse::from).toList();

        return GuestCartItemResponse.of(ProductResponse.from(productDetail.getProduct()),
                                        ProductDetailResponse.from(productDetail, optionResponseList), redisCartItem);
    }


    public GuestCartResponse readCartGuest(String cartToken) {

        boolean existKey = redisTemplate.hasKey(GUEST_CART_KEY_PREFIX + cartToken);

        List<RedisCartItemDto> list = redisTemplate.opsForHash().entries(GUEST_CART_KEY_PREFIX + cartToken)
                .values().stream().map(o -> (RedisCartItemDto) o).toList();

        if (!existKey) {
            redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofDays(1));
        }

        List<GuestCartItemResponse> cartItemResponses = list.stream()
                .map(dto -> {
                    ProductDetail productDetail = productDetailRepository.findById(dto.getProductDetailId())
                            .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

                    Product product = productDetail.getProduct();

                    List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(productDetail.getId())
                            .stream().map(OptionResponse::from).toList();

                    return GuestCartItemResponse.of(
                            ProductResponse.from(product),
                            ProductDetailResponse.from(productDetail, optionResponseList),
                            dto);
                }).toList();

        return GuestCartResponse.from(cartItemResponses);
    }

    public GuestCartItemResponse updateCartItemQuantity(String cartToken, UpdateCartItemRequest request) {

        ProductDetail productDetail = productDetailRepository.findById(request.productDetailId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        RedisCartItemDto redisCartItem =
                (RedisCartItemDto) redisTemplate.opsForHash()
                        .get(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(request.productDetailId()));

        if (redisCartItem == null) {
            throw new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        redisCartItem.updateQuantity(request.quantity());
        redisTemplate.opsForHash().put(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(request.productDetailId()), redisCartItem);

        List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(productDetail.getId())
                .stream().map(OptionResponse::from).toList();

        return GuestCartItemResponse.of(ProductResponse.from(productDetail.getProduct()),
                ProductDetailResponse.from(productDetail, optionResponseList), redisCartItem);
    }

    public void deleteCartItem(String cartToken, Long productDetailId) {
        boolean existKey = redisTemplate.hasKey(GUEST_CART_KEY_PREFIX + cartToken);

        if (!redisTemplate.opsForHash().hasKey(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetailId))) {
            throw new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        redisTemplate.opsForHash().delete(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetailId));
        if (!existKey) {
            redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofDays(1));
        }
    }

    public GuestCartResponse emptyCart(String cartToken) {
        redisTemplate.delete(GUEST_CART_KEY_PREFIX + cartToken);
        return GuestCartResponse.from(new ArrayList<>());
    }
}
