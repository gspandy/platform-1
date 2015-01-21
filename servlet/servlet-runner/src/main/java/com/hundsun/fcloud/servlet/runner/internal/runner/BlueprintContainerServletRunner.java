package com.hundsun.fcloud.servlet.runner.internal.runner;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.api.annotation.Mapping;
import com.hundsun.fcloud.servlet.api.annotation.Servlet;
import com.hundsun.fcloud.servlet.api.resolver.ServletContextResolver;
import com.hundsun.fcloud.servlet.api.resolver.ServletMappingResolver;
import com.hundsun.fcloud.servlet.runner.internal.utils.ServletBundleUtils;
import org.osgi.framework.Bundle;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gavin Hu on 2015/1/6.
 */
public class BlueprintContainerServletRunner implements ServletRunner {

    private Bundle bundle;

    private BlueprintContainer blueprintContainer;

    private ServletContextResolver servletContextResolver;

    private ServletMappingResolver servletMappingResolver;

    private Map<String, Object> mappingServletInstances = new HashMap<String, Object>();

    private Map<String, Method> mappingServletMethods = new HashMap<String, Method>();

    public BlueprintContainerServletRunner(Bundle bundle,
                                           BlueprintContainer blueprintContainer,
                                           ServletContextResolver servletContextResolver,
                                           ServletMappingResolver servletMappingResolver) {
        this.bundle = bundle;
        this.blueprintContainer = blueprintContainer;
        this.servletContextResolver = servletContextResolver;
        this.servletMappingResolver = servletMappingResolver;
    }

    @Override
    public void init() {
        //
        for(String componentId : blueprintContainer.getComponentIds()) {
            //
            Object componentInstance = blueprintContainer.getComponentInstance(componentId);
            Class<?> componentClass = componentInstance.getClass();
            //
            if(AnnotationUtils.isAnnotationDeclaredLocally(Servlet.class, componentClass)) {
                Object servletInstance = componentInstance;
                //
                Method[] servletMethods = componentClass.getDeclaredMethods();
                for(Method servletMethod : servletMethods) {
                    //
                    Mapping mapping = servletMethod.getAnnotation(Mapping.class);
                    if(mapping==null) {
                        continue;
                    }
                    //
                    for(String servletMapping : mapping.value()) {
                        //
                        this.mappingServletInstances.put(servletMapping, servletInstance);
                        this.mappingServletMethods.put(servletMapping, servletMethod);
                    }
                }
            }
            //
            if(ClassUtils.isAssignable(ServletContextResolver.class, componentClass)) {
                this.servletContextResolver = (ServletContextResolver) componentInstance;
            }
            //
            if(ClassUtils.isAssignable(ServletMappingResolver.class, componentClass)) {
                this.servletMappingResolver = (ServletMappingResolver) componentInstance;
            }
        }
    }

    @Override
    public boolean canRun(ServletRequest request, ServletResponse response) {
        String servletContext = ServletBundleUtils.getServletContext(bundle);
        String resolvedServletContext = this.servletContextResolver.resolve(request);
        return servletContext.equals(resolvedServletContext);
    }

    @Override
    public void run(ServletRequest request, ServletResponse response) throws Exception {
        String servletMapping = this.servletMappingResolver.resolve(request);
        Object servletInstance = mappingServletInstances.get(servletMapping);
        Method servletMethod = mappingServletMethods.get(servletMapping);
        //
        ReflectionUtils.invokeMethod(servletMethod, servletInstance, request, response);
    }

    @Override
    public void destroy() {
        //
        this.mappingServletInstances.clear();
        this.mappingServletMethods.clear();
    }

}
