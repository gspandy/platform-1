package com.hundsun.fcloud.servlet.codec.fixlen;

import com.hundsun.fcloud.servlet.api.ServletMessage;
import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.codec.AttributeKeys;
import com.hundsun.fcloud.servlet.codec.fixlen.config.CodecConfig;
import com.hundsun.fcloud.servlet.codec.fixlen.config.CodecConfigUtils;
import com.hundsun.fcloud.servlet.share.DefaultServletResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.Attribute;

import java.util.List;

/**
 * Created by Gavin Hu on 2014/12/29.
 */
public class FixlenResponseDecoder extends MessageToMessageDecoder<ByteBuf> {

    //private static final int FIX_LEN = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        //
        //byte[] lengthBytes = new byte[FIX_LEN];
        //byteBuf.readBytes(lengthBytes);
        //int length = Integer.parseInt(new String(lengthBytes));
        ByteBuf msgBuf = Unpooled.buffer(byteBuf.readableBytes());
        //
        msgBuf.writeBytes(byteBuf, byteBuf.readableBytes());
        //
        Attribute<ServletRequest> attribute = ctx.channel().attr(AttributeKeys.servletRequestKey);
        ServletRequest servletRequest = attribute.getAndRemove();
        String codec = servletRequest.getHeader(ServletMessage.HEADER_CODEC, String.class);
        CodecConfig codecConfig = CodecConfigUtils.getMapped(codec);
        //
        ServletMessage servletMessage = FixlenMessageCodec.decode(msgBuf.array(), codecConfig, false);
        ServletResponse servletResponse =  new DefaultServletResponse(servletMessage);
        //
        out.add(servletResponse);
    }

}
