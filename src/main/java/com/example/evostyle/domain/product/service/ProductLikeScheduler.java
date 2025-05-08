package com.example.evostyle.domain.product.service;

import com.example.evostyle.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductLikeScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final ProductRepository productRepository;

    final String PRODUCT_LIKE_COUNT_PREFIX = "productLikeCount::";

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void likeCountUpdate() {
        List<String> byteKeyList = new ArrayList<>();

        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(PRODUCT_LIKE_COUNT_PREFIX + "*")
                .count(100)
                .build();

        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                connection -> connection.scan(scanOptions))) {
            while (cursor.hasNext()) {byteKeyList.add(new String(cursor.next()));}
        }

        List<Object> result = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String key : byteKeyList) {
                connection.stringCommands().get(key.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });

        for(int i = 0 ; i < byteKeyList.size() ; i++){
            Long productId = Long.parseLong(byteKeyList.get(i).split("::")[1]);
            int count = Integer.parseInt((String.valueOf(result.get(i))));

            productRepository.updateLikeCount(productId, count);
        }
    }
}
