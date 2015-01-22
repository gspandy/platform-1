package com.hundsun.fcloud.servlet.codec.fixlen;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.codec.AttributeKeys;
import com.hundsun.fcloud.servlet.codec.fixlen.config.CodecConfig;
import com.hundsun.fcloud.servlet.codec.fixlen.config.CodecConfigUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.Attribute;

import java.util.List;

/**
 * Created by Gavin Hu on 2014/12/28.
 */
public class FixlenResponseEncoder extends MessageToMessageEncoder<ServletResponse> {

    private static final int FIX_LEN = 4;

    @Override
    protected void encode(ChannelHandlerContext ctx, ServletResponse response, List<Object> out) throws Exception {
        //
        Attribute<ServletRequest> attribute = ctx.channel().attr(AttributeKeys.servletRequestKey);
        ServletRequest servletRequest = attribute.getAndRemove();
        //
        String codec = servletRequest.getHeader(ServletRequest.HEADER_CODEC, String.class);
        CodecConfig codecConfig = CodecConfigUtils.getMapped(codec);
        //
        byte[] message = FixlenMessageCodec.encode(response, codecConfig, false);
        //String numberStr = formatNumber(FIX_LEN + message.length);
        ByteBuf byteBuf = Unpooled.buffer(FIX_LEN + message.length);
        //byteBuf.writeBytes(numberStr.getBytes());
        byteBuf.writeBytes(message);
        //
        out.add(byteBuf);
    }

    private String formatNumber(int number) {
        //
        String numberStr = String.valueOf(number);
        StringBuffer numberBuffer = new StringBuffer(numberStr);
        for(int i=0; i<4-numberStr.length(); i++) {
            numberBuffer.insert(0, "0");
        }
        return numberBuffer.toString();
    }

}
