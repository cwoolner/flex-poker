package com.flexpoker.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class StringUtils {

    static {
        ToStringBuilder.setDefaultStyle(ToStringStyle.MULTI_LINE_STYLE);
    }

    public static String allFieldsToString(Object object) {
        return ToStringBuilder.reflectionToString(object);
    }
}
