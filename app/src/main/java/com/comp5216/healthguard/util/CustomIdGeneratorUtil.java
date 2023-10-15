package com.comp5216.healthguard.util;

import java.util.UUID;

/**
 * ID生成工具类
 * <p>
 * 用于生成28位字符串，作为各个表的唯一标识符，Id
 * 基于UUID，但是对其进行了简化
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-26
 */
public class CustomIdGeneratorUtil {

    /**
     * 生成一个28字符长的唯一字符串。
     * 这个方法首先生成一个UUID，然后移除其中的短划线，并截取前28个字符。
     *
     * @return 28字符长的唯一字符串
     */
    public static String generateUniqueId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, 28);  // 去掉短划线并获取前28个字符
    }

}
