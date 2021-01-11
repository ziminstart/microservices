package com.imooc.model.vo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegionRatioVO {

    private String name;
    private Integer value;
}
