package com.example.evostyle.domain.product.service;


import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.dto.response.ProductLikeResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.entity.ProductLike;
import com.example.evostyle.domain.product.repository.ProductLikeRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductLikeService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    final String PRODUCT_LIKE_COUNT_PREFIX = "productLikeCount::";

    @Transactional
    public ProductLikeResponse createProductLike(Long memberId, Long productId){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        if(productLikeRepository.existsByMemberIdAndProductId(memberId, productId)){
            throw new ConflictException(ErrorCode.PRODUCT_LIKE_ALREADY_EXISTS);
        }

        ProductLike productLike = productLikeRepository.save(ProductLike.of(member, product));

        redisTemplate.opsForValue().increment(PRODUCT_LIKE_COUNT_PREFIX + productId);
        return ProductLikeResponse.from(productLike);
    }


    @Transactional
    public void deleteProductLike(Long memberId, Long productId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if(!productLikeRepository.existsByMemberIdAndProductId(memberId, productId)){
            throw new NotFoundException(ErrorCode.PRODUCT_LIKE_NOT_FOUND);
        }

        redisTemplate.opsForValue().decrement(PRODUCT_LIKE_COUNT_PREFIX + productId);
        productLikeRepository.deleteByMemberIdAndProductId(memberId, productId);
    }

    public List<ProductLikeResponse> readAllByMember(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        List<ProductLikeResponse> productLikeList = productLikeRepository.findByMemberId(memberId)
                .stream().map(ProductLikeResponse::from).toList();

        return productLikeList;
    }
}
