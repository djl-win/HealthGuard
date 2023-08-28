package com.comp5216.healthguard.exception;

/**
 * 查询异常处理类
 * <p>
 * 处理查询时出现的异常信息
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-08-25
 */
public class QueryException  extends RuntimeException{
    public QueryException() {
        super("Query error");
    }
}
