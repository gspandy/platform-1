package com.hundsun.fcloud.servlet.codec.fixlen.config;

/**
 * 列模型配置
 *
 * @author <a href="mailto:huhz@hundsun.com">Gavin Hu</a>
 * @since 1.0.0
 */
public class ColumnConfig {

    private String name;
    private String type;
    private String length;
    private String declen;
    private String _public;

    public ColumnConfig(String name, String type, String length, String declen, String _public) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.declen = declen;
        this._public = _public;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLengthInt() {
        return Integer.parseInt(length);
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDeclen() {
        return declen;
    }

    public void setDeclen(String declen) {
        this.declen = declen;
    }

    public boolean isPublic() {
        return Boolean.parseBoolean(_public);
    }

    public void setPublic(String _public) {
        this._public = _public;
    }
}
