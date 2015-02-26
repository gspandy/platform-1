/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.hundsun.fcloud.web.extender;

import com.hundsun.fcloud.web.extender.config.FilterConfig;
import com.hundsun.fcloud.web.extender.config.ListenerConfig;
import com.hundsun.fcloud.web.extender.config.ServletConfig;
import com.hundsun.fcloud.web.extender.config.WebConfiguration;
import com.hundsun.fcloud.web.extender.config.xml.XmlWebConfiguration;
import com.hundsun.fcloud.web.extender.context.ExtServletContext;
import com.hundsun.fcloud.web.extender.context.ExtServletContextImpl;
import com.hundsun.fcloud.web.extender.dispatch.Dispatcher;
import com.hundsun.fcloud.web.extender.fork.ForkableDispatcher;
import com.hundsun.fcloud.web.extender.handler.FilterHandler;
import com.hundsun.fcloud.web.extender.handler.HandlerRegistry;
import com.hundsun.fcloud.web.extender.handler.ServletHandler;
import com.hundsun.fcloud.web.extender.listener.ListenerRegistry;

import javax.servlet.*;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ExtenderListener</code> implements a Servlet API listener for HTTP
 * Session related events. These events are provided by the servlet container
 * and forwarded to the event dispatcher.
 *
 * @since 2.1.0
 */
public class ExtenderListener implements HttpSessionAttributeListener, HttpSessionListener,
        ServletContextListener, ServletRequestAttributeListener, ServletRequestListener {

    private ExtServletContext extServletContext;

    private HandlerRegistry handlerRegistry = new HandlerRegistry();

    private ListenerRegistry listenerRegistry = new ListenerRegistry();

    // ---------- ServletContextListener

    public void contextInitialized(final ServletContextEvent sce) {
        //
        InputStream webExtenderXml = sce.getServletContext().getResourceAsStream("/WEB-INF/web-extender.xml");
        WebConfiguration webConfiguration = new XmlWebConfiguration(webExtenderXml);
        //
        initExtServletContext(sce.getServletContext(), webConfiguration);
        //======================================================================
        initListenerRegistry(webConfiguration);
        //
        ServletContextEvent event = new ServletContextEvent(extServletContext);
        for (ServletContextListener contextListener : listenerRegistry.getServletContextListeners()) {
            contextListener.contextInitialized(event);
        }
        //=======================================================================
        initHandlerRegistry(extServletContext, webConfiguration);
        //
        Dispatcher dispatcher = new Dispatcher(handlerRegistry);
        ForkableDispatcher forkableDispatcher = new ForkableDispatcher(dispatcher, extServletContext);
        //
        sce.getServletContext().setAttribute(Dispatcher.class.getName(), dispatcher);
        sce.getServletContext().setAttribute(ForkableDispatcher.class.getName(), forkableDispatcher);
    }

    private void initExtServletContext(ServletContext servletContext, WebConfiguration webConfiguration) {
        this.extServletContext = new ExtServletContextImpl(servletContext, new ExtenderServletContextAttributeListener());
        this.extServletContext.setContextParams(webConfiguration.getContextParams());
    }

    private void initListenerRegistry(WebConfiguration webConfiguration) {
        //
        for (ListenerConfig listenerConfig : webConfiguration.getAllListenerConfig()) {
            try {
                Class<?> listenerClass = getClassLoader().loadClass(listenerConfig.getListenerClass());
                Object listener = listenerClass.newInstance();
                listenerRegistry.addListener(listener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void contextDestroyed(final ServletContextEvent sce) {
        //
        ServletContextEvent event = new ServletContextEvent(extServletContext);
        for (ServletContextListener contextListener : listenerRegistry.getServletContextListeners()) {
            contextListener.contextDestroyed(event);
        }
        //
        sce.getServletContext().removeAttribute(Dispatcher.class.getName());
        sce.getServletContext().removeAttribute(ForkableDispatcher.class.getName());
    }

    protected ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        return classLoader;
    }

    private void initHandlerRegistry(ServletContext servletContext, WebConfiguration webConfiguration) {
        //
        List<FilterConfig> filterConfigList = webConfiguration.getAllFilterConfig();
        for (FilterConfig filterConfig : filterConfigList) {
            try {
                List<FilterHandler> filterHandlers = newFilterHandlers(servletContext, filterConfig);
                for (FilterHandler filterHandler : filterHandlers) {
                    handlerRegistry.addFilter(filterHandler);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //
        List<ServletConfig> servletConfigList = webConfiguration.getAllServletConfig();
        for (ServletConfig servletConfig : servletConfigList) {
            try {
                List<ServletHandler> servletHandlers = newServletHandler(servletContext, servletConfig);
                for (ServletHandler servletHandler : servletHandlers) {
                    handlerRegistry.addServlet(servletHandler);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<FilterHandler> newFilterHandlers(ServletContext servletContext, FilterConfig filterConfig) throws Exception {
        List<FilterHandler> filterHandlers = new ArrayList<FilterHandler>();
        //
        Class<?> filterClass = getClassLoader().loadClass(filterConfig.getFilterClass());
        Filter filter = (Filter) filterClass.newInstance();
        //
        FilterHandler filterHandler = new FilterHandler(servletContext, filter, filterConfig.getFilterName(), filterConfig.getUrlPatterns(), 0);
        filterHandler.setInitParams(filterConfig.getInitParams());
        //
        filterHandlers.add(filterHandler);
        //
        return filterHandlers;
    }

    private List<ServletHandler> newServletHandler(ServletContext servletContext, ServletConfig servletConfig) throws Exception {
        List<ServletHandler> servletHandlers = new ArrayList<ServletHandler>();
        //
        Class<?> servletClass = getClassLoader().loadClass(servletConfig.getServletClass());
        Servlet servlet = (Servlet) servletClass.newInstance();
        //
        ServletHandler servletHandler = new ServletHandler(servletContext, servlet, servletConfig.getServletName(), servletConfig.getUrlPatterns());
        servletHandler.setInitParams(servletConfig.getInitParams());
        //
        servletHandlers.add(servletHandler);
        //
        return servletHandlers;
    }

    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        for(ServletRequestAttributeListener requestAttributeListener : listenerRegistry.getRequestAttributeListeners()) {
            requestAttributeListener.attributeAdded(srae);
        }
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        for(ServletRequestAttributeListener requestAttributeListener : listenerRegistry.getRequestAttributeListeners()) {
            requestAttributeListener.attributeRemoved(srae);
        }
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        for(ServletRequestAttributeListener requestAttributeListener : listenerRegistry.getRequestAttributeListeners()) {
            requestAttributeListener.attributeReplaced(srae);
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        for(ServletRequestListener requestListener : listenerRegistry.getRequestListeners()) {
            requestListener.requestDestroyed(sre);
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        for(ServletRequestListener requestListener : listenerRegistry.getRequestListeners()) {
            requestListener.requestInitialized(sre);
        }
    }

    private class ExtenderServletContextAttributeListener implements ServletContextAttributeListener {

        @Override
        public void attributeAdded(ServletContextAttributeEvent event) {

        }

        @Override
        public void attributeRemoved(ServletContextAttributeEvent event) {

        }

        @Override
        public void attributeReplaced(ServletContextAttributeEvent event) {

        }

    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        for (HttpSessionAttributeListener sessionAttributeListener : listenerRegistry.getSessionAttributeListeners()) {
            sessionAttributeListener.attributeAdded(event);
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        for (HttpSessionAttributeListener sessionAttributeListener : listenerRegistry.getSessionAttributeListeners()) {
            sessionAttributeListener.attributeRemoved(event);
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        for (HttpSessionAttributeListener sessionAttributeListener : listenerRegistry.getSessionAttributeListeners()) {
            sessionAttributeListener.attributeReplaced(event);
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        for (HttpSessionListener sessionListener : listenerRegistry.getSessionListeners()) {
            sessionListener.sessionCreated(se);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        for (HttpSessionListener sessionListener : listenerRegistry.getSessionListeners()) {
            sessionListener.sessionDestroyed(se);
        }
    }

}
