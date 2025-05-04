package com.example.evostyle.domain.brand.dto.request;

import java.util.List;

public record CreateBrandRequest(String name,String slackWebHookUrl ,List<Long> categoryIdList) {
}
