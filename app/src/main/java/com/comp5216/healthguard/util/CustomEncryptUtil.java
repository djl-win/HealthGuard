package com.comp5216.healthguard.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密算法工具类
 * <p>
 * 提供了多种加密解密算法，自行使用
 * 参考:
 * <a href="https://www.javaguides.net/2020/02/java-sha-256-hash-with-salt-example.html">...</a>"
 * <a href="https://www.javaguides.net/2020/02/java-cipher-class-example-tutorial.html">...</a>
 *
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-25
 */
public class CustomEncryptUtil {

    // 使用AES算法
    private static final String ALGORITHM = "AES";
    // 使用AES算法，ECB模式，PKCS5Padding填充
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    // 加密密钥
    private static final String AES_KEY = "fe578c3eaf1e96220e911ef17bab2570";

    /**
     * 为字符串进行SHA-256哈希加密，把加密之后的数据存储到数据库，无需解密，只需核对输入的密码加密之后和数据库中的密码一不一致即可。
     *
     * @param content 需要加密的数据
     * @return 返回使用SHA-256加密的密码。
     */
    public static String encryptBySHA256(String content) {
        String afterEncryptBySHA256 = null; // 初始化生成的密码变量
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // 获取SHA-256摘要实例
            byte[] bytes = md.digest(content.getBytes()); // 计算密码的哈希值
            StringBuilder sb = new StringBuilder(); // 创建StringBuilder来构建结果字符串
            for (byte aByte : bytes) {
                // 将每个字节转换为十六进制，并加入到StringBuilder中
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            afterEncryptBySHA256 = sb.toString(); // 转换StringBuilder为字符串
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // 如果算法不存在，打印异常
        }
        return afterEncryptBySHA256; // 返回生成的哈希值
    }

    /**
     * 为字符串进行AES加密，把加密之后的数据存储到数据库，需要解密
     *
     * @param content    要加密的消息
     * @return 加密后的内容的Base64编码字符串
     */
    public static String encryptByAES(String content)
            throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKey secretKey = new SecretKeySpec(AES_KEY.getBytes(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(encryptedMessage);

    }

    /**
     * 解密AES消息
     *
     * @param encryptedByAES 经过加密时候的字符串
     * @return 解密后的原始消息字符串
     */
    public static String decryptByAES(String encryptedByAES)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKey secretKey = new SecretKeySpec(AES_KEY.getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedMessage = Base64.getDecoder().decode(encryptedByAES);
        byte[] clearMessage = cipher.doFinal(encryptedMessage);
        return new String(clearMessage);
    }

}
