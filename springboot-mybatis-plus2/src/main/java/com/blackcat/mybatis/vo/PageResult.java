package com.blackcat.mybatis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p> 描述 : bootstrap table用到的返回json格式
 * @author : blackcat
 * @date  : 2020/1/28 11:05
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PageResult {
    private Long total;
    private List rows;
}
