package com.hundsun.fcloud.servlet.runner.internal;

import com.hundsun.fcloud.servlet.runner.internal.tracker.BlueprintServletRunnerTracker;
import com.hundsun.fcloud.servlet.runner.internal.tracker.ServletRunnerTracker;
import com.hundsun.fcloud.servlet.runner.internal.transport.DefaultServer;
import com.hundsun.fcloud.servlet.runner.internal.transport.Server;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.blueprint.container.BlueprintContainer;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Gavin Hu on 2014/12/30.
 */
public class Activator implements BundleActivator, ServiceTrackerCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(Activator.class);

    private BundleContext bundleContext;

    private BundleTracker bundleTracker;

    private ServiceTracker serviceTracker;

    private Server server;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        //
        logger.info("Servlet Runner Activator Starting!");
        //
        ServletRunnerTracker servletRunnerTracker = new BlueprintServletRunnerTracker(bundleContext);
        this.serviceTracker = new ServiceTracker(bundleContext, BlueprintContainer.class, servletRunnerTracker);
        this.serviceTracker.open();
        //
        this.server = new DefaultServer(servletRunnerTracker);
        this.server.bind(6161);
        //
        logger.info("Servlet runner activator started!");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        //
        logger.info("Servlet runner activator stopping!");
        //
        this.server.close();
        //
        this.serviceTracker.close();
        //
        logger.info("Servlet runner activator stopped!");
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
