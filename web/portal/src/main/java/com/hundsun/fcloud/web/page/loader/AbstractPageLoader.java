package com.hundsun.fcloud.web.page.loader;

import com.hundsun.fcloud.web.page.config.PageConfig;
import com.hundsun.fcloud.web.page.config.PositionConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Gavin Hu on 2015/2/26.
 */
public abstract class AbstractPageLoader implements PageLoader {

    private String prefix;

    private String suffix;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    protected void mergePageConfigFromParent(PageConfig pageConfig, PageConfig parentPageConfig) {
        // merge title
        if(pageConfig.getTitle()==null) {
            pageConfig.setTitle(parentPageConfig.getTitle());
        }
        // merge layout
        if(pageConfig.getLayout()==null) {
            pageConfig.setLayout(parentPageConfig.getLayout());
        }
        // merge position
        Set<String> positionNames = new HashSet<String>();
        for(PositionConfig positionConfig : pageConfig.getPositionConfigList()) {
            positionNames.add(positionConfig.getName());
        }
        for(PositionConfig parentPositionConfig : parentPageConfig.getPositionConfigList()) {
            if(positionNames.contains(parentPositionConfig.getName())==false) {
                pageConfig.getPositionConfigList().add(parentPositionConfig);
            }
        }
    }

    protected String getPageLocation(String name) {
        if(name.startsWith("/")) {
            return prefix + name + suffix;
        }
        return prefix + "/" + name + suffix;
    }

}
