package com.sparta.cookietalk.upload.components;

public interface Converter<INPUT, RETURN> {
    RETURN convert(INPUT input);
}
