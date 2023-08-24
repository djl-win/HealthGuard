package com.comp5216.healthguard.Exception;


/**
 * 加密异常处理类
 * <p>
 * 处理加密时出现的异常信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-25
 */
public class EncryptionException extends RuntimeException {
    public EncryptionException(Throwable cause) {
        super("Encryption error", cause);
    }
}