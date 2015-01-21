package com.hundsun.fcloud.servlet.codec.fixlen;

import com.hundsun.fcloud.servlet.api.ServletMessage;
import com.hundsun.fcloud.servlet.codec.fixlen.config.CodecConfig;
import com.hundsun.fcloud.servlet.codec.fixlen.config.ColumnConfig;
import com.hundsun.fcloud.servlet.share.DefaultServletMessage;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gavin Hu on 2014/12/29.
 */
public class FixlenMessageCodec {

    private static final Charset CHARSET = Charset.forName("GBK");

    public static ServletMessage decode(byte[] message, CodecConfig codecConfig, boolean inModel) {
        //
        int inIndex = 0;
        Map parameterMap = new HashMap();
        ServletMessage servletMessage = new DefaultServletMessage();
        servletMessage.setHeader(ServletMessage.HEADER_PARAMETER_MAP, parameterMap);
        //
        List<String> properties = inModel ? codecConfig.getInProperties() : codecConfig.getOutProperties();
        for (String property : properties) {
            ColumnConfig columnConfig = inModel ? codecConfig.getMappedInColumn(property) : codecConfig.getMappedOutColumn(property);
            //转为字节数组，不会出现因中文而引起的长度失真问题
            String columnValue = new String(message, inIndex, columnConfig.getLengthInt());
            //
            inIndex += columnConfig.getLengthInt();
            parameterMap.put(columnConfig.getName(), columnValue.trim());
        }
        //
        return servletMessage;
    }

    public static byte[] encode(ServletMessage message,CodecConfig codecConfig, boolean inMode) {
        //
        StringBuffer byteBuffer = new StringBuffer();
        //
        int totalLength = 0;
        Map<String, Object> parameterMap = message.getHeader(
                ServletMessage.HEADER_PARAMETER_MAP, new HashMap<String, Object>(), Map.class);
        //
        List<String> properties = inMode ? codecConfig.getInProperties() : codecConfig.getOutProperties();
        for(String property : properties) {
            ColumnConfig columnConfig = inMode ? codecConfig.getMappedInColumn(property) : codecConfig.getMappedOutColumn(property);
            //
            Object value = parameterMap.get(property);
            //
            String stringValue = value==null ? "" : String.valueOf(value);
            byte[] valueBytes = stringValue.getBytes();
            int valueLength = valueBytes.length;
            int valueLengthLimit = columnConfig.getLengthInt();
            //
            if(valueLength<=valueLengthLimit) {
                byteBuffer.append(stringValue);
                // append ' ' char if necessary
                for(int j=0; j<valueLengthLimit-valueLength; j++) {
                    byteBuffer.append(' ');
                }
                //
                totalLength += valueLengthLimit;
            } else {
                throw new IllegalArgumentException(
                        String.format("属性[%s]的值[%s]超出限制长度[%s]", property, stringValue, valueLengthLimit));
            }
            totalLength += valueLengthLimit;
        }
        //
        return byteBuffer.toString().getBytes(CHARSET);
    }

}
