package com.appdsn.fastdemo.weather.rxhttp;

import com.appdsn.fastdemo.weather.entity.BaseResponse;
import com.appdsn.fastdemo.weather.entity.ErrorCode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import rxhttp.wrapper.annotation.Parser;
import rxhttp.wrapper.entity.ParameterizedTypeImpl;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.parse.AbstractParser;


/**
 * Desc:
 * <p>
 *
 * @Author: wangbaozhong
 * @Date: 2020/9/8 17:04
 */
@Parser(name = "Response", wrappers = {List.class})
public class CommonResponseParser<T> extends AbstractParser<T> {
    //注意，以下两个构造方法是必须的
    protected CommonResponseParser() {
        super();
    }

    public CommonResponseParser(Type type) {
        super(type);
    }

    @Override
    public T onParse(okhttp3.Response response) throws IOException {
        final Type type = ParameterizedTypeImpl.get(BaseResponse.class, mType); //获取泛型类型
        BaseResponse<T> data = convert(response, type);
        T t = data.getData(); //获取data字段
        if (data.getCode() != ErrorCode.SUCCESS || t == null) {//这里假设code不等于200，代表数据不正确，抛出异常
            throw new ParseException(String.valueOf(data.getCode()), data.getMsg(), response);
        }
        return t;
    }
}
