package com.hundsun.fcloud.web.page.config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Widget 配置
 * TODO Performance Tuning
 * @author <a href="mailto:huhz@hundsun.com">Gavin Hu</a>
 * @since 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageletConfig {

    @XmlAttribute
    private String id;
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String mode;
    @XmlAttribute
    private String when;
    @XmlAttribute
    private String path;
    @XmlAttribute
    private String context;
    @XmlAttribute
    private int cache;
    // for order
    @XmlAttribute
    private int order;
    //
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "prop")
    private List<PropConfig> propConfigList = new ArrayList<PropConfig>();
    @XmlElementWrapper(name = "view-events")
    @XmlElement(name = "event")
    private List<EventConfig> viewEventConfigList = new ArrayList<EventConfig>();
    @XmlElementWrapper(name = "action-events")
    @XmlElement(name = "event")
    private List<EventConfig> actionEventConfigList = new ArrayList<EventConfig>();
    //
    private Properties properties = new Properties();
    //
    public String getId() {
        if (id == null) {
            this.id = generateId();
        }
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getCache() {
        return cache;
    }

    public void setCache(int cache) {
        this.cache = cache;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Properties getProperties() {
        if(properties.isEmpty()) {
            for(PropConfig propConfig : propConfigList) {
                properties.setProperty(propConfig.getName(), propConfig.getValue());
            }
        }
        return properties;
    }

    public List<PropConfig> getPropConfigList() {
        return propConfigList;
    }

    public void setPropConfigList(List<PropConfig> propConfigList) {
        this.propConfigList = propConfigList;
    }

    public List<EventConfig> getViewEventConfigList() {
        return viewEventConfigList;
    }

    public void setViewEventConfigList(List<EventConfig> viewEventConfigList) {
        this.viewEventConfigList = viewEventConfigList;
    }

    public List<EventConfig> getActionEventConfigList() {
        return actionEventConfigList;
    }

    public void setActionEventConfigList(List<EventConfig> actionEventConfigList) {
        this.actionEventConfigList = actionEventConfigList;
    }

    private String generateId() {
        String uuidString = UUID.randomUUID().toString();
        return uuidString.replaceAll("-", "");
    }

}
