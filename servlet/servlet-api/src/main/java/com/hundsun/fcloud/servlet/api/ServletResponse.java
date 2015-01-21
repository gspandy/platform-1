package com.hundsun.fcloud.servlet.api;

import java.io.OutputStream;

/**
 * Servlet响应
 *
 * @author gavin
 * @create 13-7-28
 * @since 1.0.0
 */
public interface ServletResponse extends ServletMessage {

   void setContentType(String contentType);

   void setParameter(String name, Object value);

   Object getParameter(String name);

   OutputStream getOutputStream();

}
