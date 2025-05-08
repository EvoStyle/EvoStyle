package com.example.evostyle.domain.cart.service;

import com.example.evostyle.domain.cart.dto.request.AddCartItemRequest;
import com.example.evostyle.domain.cart.dto.request.UpdateCartItemRequest;
import com.example.evostyle.domain.cart.dto.response.GuestCartItemResponse;
import com.example.evostyle.domain.cart.dto.response.GuestCartResponse;
import com.example.evostyle.domain.cart.dto.service.RedisCartItemDto;
import com.example.evostyle.domain.product.dto.response.OptionResponse;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;

import com.example.evostyle.domain.product.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.OptionRepository;
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


    public GuestCartItemResponse addCartItem(AddCartItemRequest request, String cartToken) {

        ProductDetail productDetail = productDetailRepository.findById(request.productDetailId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        if (redisTemplate.opsForHash().hasKey(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetail.getId()))) {
            throw new ConflictException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        }

        RedisCartItemDto redisCartItem = RedisCartItemDto.of(request.productDetailId(), request.quantity());

        redisTemplate.opsForHash().put(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetail.getId()), redisCartItem);
        redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofMinutes(15));// 사용자의 마지막 사용시점을 기점으로 갱신해나가는 방식

        List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(productDetail.getId())
                .stream().map(OptionResponse::from).toList();

        return GuestCartItemResponse.of(
                productDetail.getProduct().getPrice(),
                ProductDetailResponse.from(productDetail, optionResponseList),
                redisCartItem
        );
    }


    public GuestCartResponse readCart(String cartToken) {

        List<RedisCartItemDto> redisCartItemDtoList = redisTemplate.opsForHash().entries(GUEST_CART_KEY_PREFIX + cartToken)
                                      .values().stream().map(o -> (RedisCartItemDto) o).toList();

        redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofMinutes(15));

        List<GuestCartItemResponse> cartItemResponses = redisCartItemDtoList.stream()
                .map(dto -> {
                    ProductDetail productDetail = productDetailRepository.findById(dto.getProductDetailId())
                            .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

                    List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(productDetail.getId())
                            .stream().map(OptionResponse::from).toList();

                    return GuestCartItemResponse.of(
                            productDetail.getProduct().getPrice(),
                            ProductDetailResponse.from(productDetail, optionResponseList),
                            dto);
                }).toList();

        return GuestCartResponse.from(cartItemResponses);
    }

    public GuestCartItemResponse updateCartItemQuantity(String cartToken, UpdateCartItemRequest request, Long productDetailId) {

        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        RedisCartItemDto redisCartItem = (RedisCartItemDto) redisTemplate.opsForHash()
                        .get(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetailId));

        if (redisCartItem == null) {throw new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND);}

        redisCartItem.updateQuantity(request.quantity());
        redisTemplate.opsForHash().put(GUEST_CART_KEY_PREFIX + cartToken, String.valueOf(productDetailId), redisCartItem);
        redisTemplate.expire(GUEST_CART_KEY_PREFIX + cartToken, Duration.ofMinutes(15));

        List<OptionResponse> optionResponseList = optionRepository.findByProductDetailId(productDetail.getId())
                .stream().map(OptionResponse::from).toList();

        return GuestCartItemResponse.of(productDetail.getProduct().getPrice(),
                ProductDetailResponse.from(productDetail, optionResponseList), redisCartItem);
    }

    public void deleteCartItem(String cartToken, Long productDetailId) {

        String redisKey = GUEST_CART_KEY_PREFIX + cartToken;
        String strProductDetailId = String.valueOf(productDetailId);

        if (!redisTemplate.opsForHash().hasKey(redisKey, strProductDetailId)) {throw new NotFoundException(ErrorCode.CART_ITEM_NOT_FOUND);}
        redisTemplate.opsForHash().delete(redisKey, strProductDetailId);
    }

    public GuestCartResponse emptyCart(String cartToken) {
        redisTemplate.delete(GUEST_CART_KEY_PREFIX + cartToken);
        return GuestCartResponse.from(new ArrayList<>());
    }
}
