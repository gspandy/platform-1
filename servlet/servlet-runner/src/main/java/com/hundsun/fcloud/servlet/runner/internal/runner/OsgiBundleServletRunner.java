package com.hundsun.fcloud.servlet.runner.internal.runner;

import com.hundsun.fcloud.servlet.api.ServletRequest;
import com.hundsun.fcloud.servlet.api.ServletResponse;
import com.hundsun.fcloud.servlet.api.annotation.Mapping;
import com.hundsun.fcloud.servlet.api.annotation.Servlet;
import com.hundsun.fcloud.servlet.api.resolver.ServletContextResolver;
import com.hundsun.fcloud.servlet.api.resolver.ServletMappingResolver;
import com.hundsun.fcloud.servlet.runner.internal.utils.ServletBundleUtils;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.osgi.io.OsgiBundleResourceLoader;
import org.springframework.osgi.io.OsgiBundleResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gavin Hu on 2014/12/31.
 */
public class OsgiBundleServletRunner implements ServletRunner {

    private static final Logger logger = LoggerFactory.getLogger(OsgiBundleServletRunner.class);

    private Bundle bundle;

    private String servletContext;

    private ServletContextResolver servletContextResolver;

    private ServletMappingResolver servletMappingResolver;

    private Map<String, Object> mappingServletInstances = new HashMap<String, Object>();

    private Map<String, Method> mappingServletMethods = new HashMap<String, Method>();

    public OsgiBundleServletRunner(Bundle bundle,
                                   ServletContextResolver servletContextResolver,
                                   ServletMappingResolver servletMappingResolver) {
        this.bundle = bundle;
        this.servletContextResolver = servletContextResolver;
        this.servletMappingResolver = servletMappingResolver;
    }

    @Override
    public void init() {
        //
        try {
            initServlets(bundle);
        } catch (Exception e) {
            //
            logger.error("Servlet runner init exception!", e);
        }
    }

    @Override
    public boolean canRun(ServletRequest request, ServletResponse response) {
        //
        String servletContext = this.servletContextResolver.resolve(request);
        return (this.servletContext).equals(servletContext);
    }

    @Override
    public void run(ServletRequest request, ServletResponse response) throws Exception {
        //
        String servletMapping = this.servletMappingResolver.resolve(request);
        Object servletInstance = mappingServletInstances.get(servletMapping);
        Method servletMethod = mappingServletMethods.get(servletMapping);
        //
        ReflectionUtils.invokeMethod(servletMethod, servletInstance, request, response);
    }

    @Override
    public void destroy() {
        //
        destroyServlets(bundle);
    }

    private void destroyServlets(Bundle bundle) {
        //
        this.mappingServletInstances.clear();
        this.mappingServletMethods.clear();
    }

    private void initServlets(Bundle bundle) throws Exception {
        //
        ResourceLoader resourceLoader = new OsgiBundleResourceLoader(bundle);
        ResourcePatternResolver rpr = new OsgiBundleResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(resourceLoader);
        //
        Resource[] classResources = rpr.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/**/*.class");
        for(Resource classResource : classResources) {
            //
            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(classResource);
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            //
            Class<?> clazz = bundle.loadClass(classMetadata.getClassName());
            if(AnnotationUtils.isAnnotationDeclaredLocally(Servlet.class, clazz)) {
                Object servletInstance = clazz.newInstance();
                //
                Method[] servletMethods = clazz.getDeclaredMethods();
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
            if(ClassUtils.isAssignable(ServletContextResolver.class, clazz)) {
                this.servletContextResolver = (ServletContextResolver) clazz.newInstance();
            }
            //
            if(ClassUtils.isAssignable(ServletMappingResolver.class, clazz)) {
                this.servletMappingResolver = (ServletMappingResolver) clazz.newInstance();
            }
        }
        //
        this.servletContext = ServletBundleUtils.getServletContext(bundle);
    }

}
