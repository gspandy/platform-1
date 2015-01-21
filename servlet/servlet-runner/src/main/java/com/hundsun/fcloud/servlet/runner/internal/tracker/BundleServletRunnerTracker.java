package com.hundsun.fcloud.servlet.runner.internal.tracker;

import com.hundsun.fcloud.servlet.api.resolver.ServletContextResolver;
import com.hundsun.fcloud.servlet.api.resolver.ServletMappingResolver;
import com.hundsun.fcloud.servlet.runner.internal.resolver.DsServletContextResolver;
import com.hundsun.fcloud.servlet.runner.internal.resolver.DsServletMappingResolver;
import com.hundsun.fcloud.servlet.runner.internal.runner.OsgiBundleServletRunner;
import com.hundsun.fcloud.servlet.runner.internal.runner.ServletRunner;
import com.hundsun.fcloud.servlet.runner.internal.utils.ServletBundleUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gavin Hu on 2014/12/31.
 */
public class BundleServletRunnerTracker implements BundleTrackerCustomizer, ServletRunnerTracker {

    private Map<String, ServletRunner> servletRunnerMap = new HashMap<String, ServletRunner>();

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        //
        if(ServletBundleUtils.hasServletAPI(bundle)) {
            //
            ServletContextResolver servletContextResolver = new DsServletContextResolver();
            ServletMappingResolver servletMappingResolver = new DsServletMappingResolver();
            //
            ServletRunner servletRunner = new OsgiBundleServletRunner(bundle, servletContextResolver, servletMappingResolver);
            servletRunner.init();
            //
            String servletContext = ServletBundleUtils.getServletContext(bundle);
            this.servletRunnerMap.put(servletContext, servletRunner);
        }
        //
        return bundle;
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
        // ignore
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        //
        if(ServletBundleUtils.hasServletAPI(bundle)) {
            //
            String servletContext = ServletBundleUtils.getServletContext(bundle);
            ServletRunner servletRunner = this.servletRunnerMap.remove(servletContext);
            servletRunner.destroy();
        }
    }

    public Collection<ServletRunner> getServletRunners() {
        return Collections.unmodifiableCollection(servletRunnerMap.values());
    }

    @Override
    public Object addingService(ServiceReference reference) {
        return null;
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service) {

    }

    @Override
    public void removedService(ServiceReference reference, Object service) {

    }
}
