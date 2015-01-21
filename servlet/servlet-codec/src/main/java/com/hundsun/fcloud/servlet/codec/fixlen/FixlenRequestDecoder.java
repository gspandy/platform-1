package com.hundsun.fcloud.servlet.codec.fixlen;

import com.hundsun.fcloud.servlet.api.ServletMessage;
import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.codec.AttributeKeys;
import com.hundsun.fcloud.servlet.codec.fixlen.config.CodecConfig;
import com.hundsun.fcloud.servlet.codec.fixlen.config.CodecConfigUtils;
import com.hundsun.fcloud.servlet.share.DefaultServletRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.Attribute;

import java.util.List;

/**
 * Created by Gavin Hu on 2014/12/28.
 */
public class FixlenRequestDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static final int FIX_LEN = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        //
        byteBuf.resetReaderIndex();
        byte[] lengthBytes = new byte[FIX_LEN];
        byteBuf.readBytes(lengthBytes);
        int length = Integer.parseInt(new String(lengthBytes));
        //
        ByteBuf msgBuf = Unpooled.buffer(length-FIX_LEN);
        msgBuf.writeBytes(byteBuf, msgBuf.capacity());
        //
        byte[] message = msgBuf.array();
        //
        String codec = new String(message, 0, 2);
        CodecConfig codecConfig = CodecConfigUtils.getMapped(codec);
        //
        ServletMessage servletMessage = FixlenMessageCodec.decode(message, codecConfig, true);
        ServletRequest servletRequest =  new DefaultServletRequest(servletMessage);
        servletRequest.setHeader(ServletRequest.HEADER_CODEC, codec);
        //
        Attribute attribute = ctx.channel().attr(AttributeKeys.servletRequestKey);
        attribute.set(servletRequest);
        //
        out.add(servletRequest);
    }

}
