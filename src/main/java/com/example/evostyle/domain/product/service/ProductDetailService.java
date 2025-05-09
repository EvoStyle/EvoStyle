package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.product.dto.request.UpdateProductDetailRequest;
import com.example.evostyle.domain.product.dto.response.ProductDetailResponse;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.dto.response.OptionQueryDto;
import com.example.evostyle.domain.product.dto.response.OptionResponse;
import com.example.evostyle.domain.product.entity.Option;
import com.example.evostyle.domain.product.repository.OptionGroupRepository;
import com.example.evostyle.domain.product.repository.OptionRepository;
import com.example.evostyle.domain.product.entity.ProductDetail;
import com.example.evostyle.domain.product.entity.ProductDetailOption;
import com.example.evostyle.domain.product.repository.ProductDetailOptionRepository;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.domain.product.repository.ProductRepository;
import com.example.evostyle.global.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductRepository productRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailOptionRepository productDetailOptionRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createProductDetail(Long memberId, Long productId) {

        if(!memberRepository.existsById(memberId)){throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);}

        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        List<Long> optionGroupIdList = optionGroupRepository.findIdByProductId(productId);

        Set<Set<Long>> existingCombinationSet = new HashSet<>(productDetailOptionRepository.findAll()
                .stream().collect(Collectors.groupingBy(pdo -> pdo.getProductDetail().getId(),
                        Collectors.mapping(pdo -> pdo.getOption().getId(), Collectors.toSet()))).values());

        Map<Long, List<Option>> allOptionMap = optionRepository.findByOptionGroupIdIn(optionGroupIdList)
                .stream()
                .collect(groupingBy(o -> o.getOptionGroup().getId())); // 옵션들을 그룹별로 묶어놓는다

        List<ProductDetail> productDetailList = new ArrayList<>();
        List<ProductDetailOption> productDetailOptionList = new ArrayList<>();

        generateCombinations(allOptionMap, 0, new ArrayList<>(), product, product.getBrand(),
                            productDetailList, productDetailOptionList, existingCombinationSet);

        productDetailRepository.saveAll(productDetailList);
        productDetailOptionRepository.saveAll(productDetailOptionList);
    }

    //싱픔이 가지는 옵션의 모든 조합을 만들어 저장한다

    private void generateCombinations(Map<Long, List<Option>> allOptionMap, int depth, List<Long> combinationList, Product product, Brand brand,
                                      List<ProductDetail> productDetailList, List<ProductDetailOption> productDetailOptionList, Set<Set<Long>> existingCombinationSet) {

        List<Long> optionGroupIds = new ArrayList<>(allOptionMap.keySet());

        if (depth == allOptionMap.size()) {//모든 옵션그룹에서 하나씩 가져왔다면

            Set<Long> newCombination = new HashSet<>(combinationList);
            if (existingCombinationSet.contains(newCombination)) {return;}

            ProductDetail productDetail = ProductDetail.of(product); //새로운 옵션 디테일을 만들고
            productDetailList.add(productDetail);

            for (Long optionId : combinationList) {
                Option option = optionRepository.findById(optionId).orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_NOT_FOUND));
                productDetailOptionList.add(ProductDetailOption.of(productDetail, option));//옵션과 상품디테일의 중간 객체를 만들어준다
            }
            return;
        }
        Long currentOptionGroupId = optionGroupIds.get(depth);
        List<Option> options = allOptionMap.get(currentOptionGroupId);

        for (Option option : options) {
            combinationList.add(option.getId());
            generateCombinations(allOptionMap, depth + 1, combinationList, product, brand,
                                    productDetailList, productDetailOptionList, existingCombinationSet);

            combinationList.remove(combinationList.size() - 1);
        }
    }


    public List<ProductDetailResponse> readByProductId(Long productId) {

        List<ProductDetailResponse> responseList = new ArrayList<>();

        List<ProductDetail> productDetailList = productDetailRepository.findByProductId(productId);
        List<Long> productDetailIdList = productDetailList.stream().mapToLong(ProductDetail::getId).boxed().toList();

        Map<Long, List<OptionQueryDto>> optionGroupMap = optionRepository.findOptionByProductDetailId(productDetailIdList)
                .stream()
                .collect(groupingBy(OptionQueryDto::productDetailId));

        for (Long productDetailId : productDetailIdList) {
            ProductDetail productDetail = productDetailList.stream()
                    .filter(p -> p.getId().equals(productDetailId))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));


            List<OptionResponse> optionResponseList = Optional.ofNullable(optionGroupMap.get(productDetailId))
                    .orElseThrow(() -> new NotFoundException(ErrorCode.OPTION_NOT_FOUND))
                    .stream().map(OptionResponse::from).toList();

            responseList.add(ProductDetailResponse.from(productDetail, optionResponseList));
        }
        return responseList;
    }


    public ProductDetailResponse readProductDetailById(Long productDetailId) {
        ProductDetail productDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND));

        List<OptionResponse> optionResponseList = productDetailOptionRepository.findByProductDetailId(productDetailId).stream()
                .map(ProductDetailOption::getOption)
                .map(OptionResponse::from).toList();

        return ProductDetailResponse.from(productDetail, optionResponseList);
    }


    @Transactional
    public List<ProductDetailResponse> updateProductDetailStock(List<UpdateProductDetailRequest> requestList, Long productId, Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        Long brandOwnerId = product.getBrand().getMember().getId();
        if (!brandOwnerId.equals(memberId)) {
            throw new ForbiddenException(ErrorCode.NOT_BRAND_OWNER);
        }

        Map<Long, Integer> requestMap = requestList.stream().collect(Collectors.toMap(UpdateProductDetailRequest::productDetailId, UpdateProductDetailRequest::stock));

        List<ProductDetail> productDetailList = productDetailRepository.findAllById(requestMap.keySet());
        if (requestList.size() != productDetailList.size()) {
            throw new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND);
        }

        productDetailList.forEach(p -> {
            if (p.getStock() != 0) {
                throw new ConflictException(ErrorCode.STOCK_MODIFICATION_NOT_ALLOWED);
            }
            if (!p.getProduct().getId().equals(productId)) {
                throw new ConflictException(ErrorCode.PRODUCT_DETAIL_MISMATCH);
            }

            p.setStock(requestMap.get(p.getId()));
        });
        return readByProductId(productId);
    }
}
