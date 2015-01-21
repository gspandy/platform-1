package com.hundsun.fcloud.servlet.runner.internal.tracker;

import com.hundsun.fcloud.servlet.runner.internal.runner.ServletRunner;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.Collection;

/**
 * Created by Gavin Hu on 2015/1/6.
 */
public interface ServletRunnerTracker extends ServiceTrackerCustomizer {

    Collection<ServletRunner> getServletRunners();

}
