package com.hundsun.fcloud.web.page.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Position 配置
 *
 * @author <a href="mailto:huhz@hundsun.com">Gavin Hu</a>
 * @since 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PositionConfig {
    //
    @XmlAttribute(name = "name")
    private String name;
    @XmlElement(name = "pagelet")
    private List<PageletConfig> pageletConfigList = new ArrayList<PageletConfig>();
    @XmlAttribute(name = "layout")
    private String layout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PageletConfig> getPageletConfigList() {
        return pageletConfigList;
    }

    public void setPageletConfigList(List<PageletConfig> pageletConfigList) {
        this.pageletConfigList = pageletConfigList;
    }

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

}
