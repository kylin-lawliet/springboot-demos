package com.blackcat.mybatis.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author zjjlive)
 * @version 1.0
 * @website https://www.foreknow.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuConditionVO extends BaseConditionVO {
    private String type;
}

