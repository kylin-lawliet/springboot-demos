package com.blackcat.mybatis.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p> ：测试用户
 * @author : blackcat
 * @date : 2020/1/18 12:58
 */
@Data
@NoArgsConstructor// 生成一个无参数的构造方法
@AllArgsConstructor// 会生成一个包含所有变量的构造方法
@TableName("TB_USER")
public class User extends Model<User> {
    @TableId(value = "USER_ID", type = IdType.AUTO)
    private Integer userId;

    @TableField("USER_NAME")
    private String userName;
}
