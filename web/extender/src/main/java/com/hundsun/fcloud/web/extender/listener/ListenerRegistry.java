package com.hundsun.fcloud.web.extender.listener;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin Hu on 2015/2/9.
 */
public class ListenerRegistry {

    private List<ServletContextListener> servletContextListeners = new ArrayList<ServletContextListener>();

    private List<HttpSessionListener> sessionListeners = new ArrayList<HttpSessionListener>();

    private List<HttpSessionAttributeListener> sessionAttributeListeners = new ArrayList<HttpSessionAttributeListener>();

    private List<HttpSessionActivationListener> sessionActivationListeners = new ArrayList<HttpSessionActivationListener>();

    private List<ServletRequestListener> requestListeners = new ArrayList<ServletRequestListener>();

    private List<ServletRequestAttributeListener> requestAttributeListeners = new ArrayList<ServletRequestAttributeListener>();

    public void addListener(Object listener) {
        //
        if(listener instanceof ServletContextListener) {
            this.servletContextListeners.add((ServletContextListener) listener);
        } else if(listener instanceof HttpSessionListener) {
            this.sessionListeners.add((HttpSessionListener) listener);
        } else if(listener instanceof HttpSessionAttributeListener) {
            this.sessionAttributeListeners.add((HttpSessionAttributeListener) listener);
        } else if(listener instanceof HttpSessionActivationListener) {
            this.sessionActivationListeners.add((HttpSessionActivationListener) listener);
        } else if(listener instanceof ServletRequestListener) {
            this.requestListeners.add((ServletRequestListener) listener);
        } else if(listener instanceof ServletRequestAttributeListener) {
            this.requestAttributeListeners.add((ServletRequestAttributeListener) listener);
        } else {
            throw new UnsupportedOperationException(listener.getClass().getName());
        }
    }

    public List<ServletContextListener> getServletContextListeners() {
        return servletContextListeners;
    }

    public List<HttpSessionListener> getSessionListeners() {
        return sessionListeners;
    }

    public List<HttpSessionAttributeListener> getSessionAttributeListeners() {
        return sessionAttributeListeners;
    }

    public List<HttpSessionActivationListener> getSessionActivationListeners() {
        return sessionActivationListeners;
    }

    public List<ServletRequestListener> getRequestListeners() {
        return requestListeners;
    }

    public List<ServletRequestAttributeListener> getRequestAttributeListeners() {
        return requestAttributeListeners;
    }

}
