package com.guludoc.learning.u3app.uaa.config;



import com.guludoc.learning.u3app.uaa.domain.dto.MfaType;
import org.springframework.core.convert.converter.Converter;

public class StringToMfaTypeConverter implements Converter<String, MfaType> {
    @Override
    public MfaType convert(String s) {
        return MfaType.fromValue(s);
    }

}
