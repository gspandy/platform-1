package com.hundsun.fcloud.servlet.codec.fixlen.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口映射配置信息
 *
 * @author <a href="mailto:huhz@hundsun.com">Gavin Hu</a>
 * @since 1.0.0
 */
public class CodecConfig {

    private static final String DIRECTION_IN = "in";

    private static final String DIRECTION_OUT = "out";

    private int publicOutPropertiesLength = 0;

    private int privateOutPropertiesLength = 0;

    private List<String> inProperties = new ArrayList<String>();

    private List<String> publicInProperties = new ArrayList<String>();

    private List<String> privateInProperties = new ArrayList<String>();

    private List<String> outProperties = new ArrayList<String>();

    private List<String> publicOutProperties = new ArrayList<String>();

    private List<String> privateOutProperties = new ArrayList<String>();

    private Map<String, String> mappedProperties = new HashMap<String, String>();

    private Map<String, ColumnConfig> mappedColumns = new HashMap<String, ColumnConfig>();

    public void addPropertyColumn(String property, String direction, ColumnConfig columnConfig) {
        //
        String directionProperty = null;
        String directionColumn = null;
        //
        if("in".equalsIgnoreCase(direction)) {
            if(inProperties.contains(property)) {
                throw new IllegalArgumentException(String.format("[direction=%s] 属性字段%s已定义！", direction, property));
            }
            this.inProperties.add(property);
            directionProperty = DIRECTION_IN + "." + property;
            directionColumn = DIRECTION_IN + "." + columnConfig.getName();
            //
            if(columnConfig.isPublic()) {
                this.publicInProperties.add(property);
            } else {
                this.privateInProperties.add(property);
            }
        }
        if("out".equalsIgnoreCase(direction)) {
            if(outProperties.contains(property)) {
                throw new IllegalArgumentException(String.format("[direction=%s] 属性字段%s已定义！", direction, property));
            }
            this.outProperties.add(property);
            directionProperty = DIRECTION_OUT + "." + property;
            directionColumn = DIRECTION_OUT + "." + columnConfig.getName();
            //
            if(columnConfig.isPublic()) {
                this.publicOutProperties.add(property);
            } else {
                this.privateOutProperties.add(property);
            }
        }
        //
        this.mappedProperties.put(directionColumn, directionProperty);
        this.mappedColumns.put(directionProperty, columnConfig);
    }

    public List<String> getInProperties() {
        return inProperties;
    }

    public List<String> getPublicInProperties() {
        return publicInProperties;
    }

    public List<String> getPrivateInProperties() {
        return privateInProperties;
    }

    public List<String> getOutProperties() {
        return outProperties;
    }

    public List<String> getPublicOutProperties() {
        return publicOutProperties;
    }

    public List<String> getPrivateOutProperties() {
        return privateOutProperties;
    }

    public ColumnConfig getMappedInColumn(String property) {
        return this.mappedColumns.get(DIRECTION_IN + "." + property);
    }

    public ColumnConfig getMappedOutColumn(String property) {
        return this.mappedColumns.get(DIRECTION_OUT + "." + property);
    }

    public int getPublicOutPropertiesLength() {
        if(publicOutPropertiesLength==0) {
            for(String property : publicOutProperties) {
                ColumnConfig columnConfig = getMappedOutColumn(property);
                this.publicOutPropertiesLength += columnConfig.getLengthInt();
            }
        }
        //
        return publicOutPropertiesLength;
    }

    public int getPrivateOutPropertiesLength() {
        if(privateOutPropertiesLength==0) {
            for(String property : privateOutProperties) {
                ColumnConfig columnConfig = getMappedOutColumn(property);
                this.privateOutPropertiesLength += columnConfig.getLengthInt();
            }
        }
        //
        return privateOutPropertiesLength;
    }

}
