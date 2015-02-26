package com.hundsun.fcloud.web.page.config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Page 配置
 *
 * @author <a href="mailto:huhz@hundsun.com">Gavin Hu</a>
 * @since 1.0.0
 */
@XmlRootElement(name = "page")
@XmlAccessorType(XmlAccessType.FIELD)
public class PageConfig {

    @XmlAttribute
    private String title;
    @XmlAttribute
    private String path;
    @XmlAttribute
    private String layout;
    @XmlAttribute
    private String parent;
    //
    @XmlElement(name = "position")
    private List<PositionConfig> positionConfigList = new ArrayList<PositionConfig>();
    //
    private Map<String, PageletConfig> pageletConfigMap = new HashMap<String, PageletConfig>();

    public PageConfig() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<PositionConfig> getPositionConfigList() {
        return positionConfigList;
    }

    public void setPositionConfigList(List<PositionConfig> positionConfigList) {
        this.positionConfigList = positionConfigList;
    }

    public List<PageletConfig> getAllPageletConfig() {
        if(pageletConfigMap.isEmpty()) {
            fillPageletConfigMap();
        }
        return new ArrayList<PageletConfig>(pageletConfigMap.values());
    }

    public boolean hasPagelet(String pageletId) {
        if(pageletConfigMap.isEmpty()) {
            fillPageletConfigMap();
        }
        return pageletConfigMap.containsKey(pageletId);
    }

    public PageletConfig getWidgetConfig(String widgetId) {
        if(pageletConfigMap.isEmpty()) {
            fillPageletConfigMap();
        }
        return pageletConfigMap.get(widgetId);
    }

    private void fillPageletConfigMap() {
        for(PositionConfig positionConfig : positionConfigList) {
            for(PageletConfig pageletConfig : positionConfig.getPageletConfigList()) {
                this.pageletConfigMap.put(pageletConfig.getId(), pageletConfig);
            }
        }
    }

}
