package com.example.tomatomall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResultVO<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

    public static <T> ResultVO<T> buildSuccess(T data) {
        return new ResultVO<T>("200", null, data);
    }

    public static <T> ResultVO<T> buildFailure(String msg) {
        return new ResultVO<T>("400", msg, null);
    }

    public static <T> ResultVO<T> notLogin() { return new ResultVO<T>("401", "not login", null); }
}
