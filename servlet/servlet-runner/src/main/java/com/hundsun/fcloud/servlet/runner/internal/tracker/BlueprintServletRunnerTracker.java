package com.hundsun.fcloud.servlet.runner.internal.tracker;

import com.hundsun.fcloud.servlet.api.resolver.ServletContextResolver;
import com.hundsun.fcloud.servlet.api.resolver.ServletMappingResolver;
import com.hundsun.fcloud.servlet.runner.internal.resolver.DsServletContextResolver;
import com.hundsun.fcloud.servlet.runner.internal.resolver.DsServletMappingResolver;
import com.hundsun.fcloud.servlet.runner.internal.runner.BlueprintContainerServletRunner;
import com.hundsun.fcloud.servlet.runner.internal.runner.ServletRunner;
import com.hundsun.fcloud.servlet.runner.internal.utils.ServletBundleUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gavin Hu on 2015/1/6.
 */
public class BlueprintServletRunnerTracker implements ServiceTrackerCustomizer, ServletRunnerTracker {

    private BundleContext bundleContext;

    private Map<String, ServletRunner> servletRunnerMap = new HashMap<String, ServletRunner>();

    public BlueprintServletRunnerTracker(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Override
    public Object addingService(ServiceReference reference) {
        //
        Bundle bundle = reference.getBundle();
        if(ServletBundleUtils.hasServletAPI(bundle)) {
            BlueprintContainer blueprintContainer = (BlueprintContainer) bundleContext.getService(reference);
            //
            ServletContextResolver servletContextResolver = new DsServletContextResolver();
            ServletMappingResolver servletMappingResolver = new DsServletMappingResolver();
            //
            ServletRunner servletRunner = new BlueprintContainerServletRunner(bundle, blueprintContainer, servletContextResolver, servletMappingResolver);
            servletRunner.init();
            //
            String servletContext = ServletBundleUtils.getServletContext(bundle);
            this.servletRunnerMap.put(servletContext, servletRunner);
        }
        //
        return reference;
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service) {}

    @Override
    public void removedService(ServiceReference reference, Object service) {
        //
        Bundle bundle = reference.getBundle();
        if(ServletBundleUtils.hasServletAPI(bundle)) {
            //
            String servletContext = ServletBundleUtils.getServletContext(bundle);
            ServletRunner servletRunner = this.servletRunnerMap.remove(servletContext);
            servletRunner.destroy();
        }
    }


    @Override
    public Collection<ServletRunner> getServletRunners() {
        return Collections.unmodifiableCollection(servletRunnerMap.values());
    }
}
