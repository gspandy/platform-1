package com.hundsun.fcloud.servlet.codec.fixlen.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper 配置解析器
 *
 * @author <a href="mailto:huhz@hundsun.com">Gavin Hu</a>
 * @since 1.0.0
 */
public class CodecConfigUtils {

    private static final Map<String, CodecConfig> codecConfigCache = new HashMap<String, CodecConfig>();

    public static CodecConfig getMapped(String codec) {
        CodecConfig codecConfig = codecConfigCache.get(codec);
        if(codecConfig==null) {
            codecConfig = loadByMapping(codec);
            codecConfigCache.put(codec, codecConfig);
        }
        //
        return codecConfig;
    }

    private static CodecConfig loadByMapping(String codec) {
        //
        try {
            String location = String.format("META-INF/codec/%s_Codec.xml", codec);
            URL url = CodecConfigUtils.class.getClassLoader().getResource(location);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openStream());
            return parseDocument(doc.getDocumentElement());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static CodecConfig parseDocument(Element rootEle) throws Exception {
        //
        CodecConfig codecConfig = new CodecConfig();
        //
        List<Element> propertyEles = getChildElements(rootEle);
        for(Element propertyEle : propertyEles) {
            String property = propertyEle.getAttribute("name");
            String direction = propertyEle.getAttribute("direction");
            //
            Element columnEle = getChildElementByTagName(propertyEle, "column");
            String columnName = columnEle.getAttribute("name");
            String columnType = columnEle.getAttribute("type");
            String columnLength = columnEle.getAttribute("length");
            String columnDeclen = columnEle.getAttribute("declen");
            String columnPublic = columnEle.getAttribute("public");
            //
            ColumnConfig columnConfig = new ColumnConfig(columnName, columnType, columnLength, columnDeclen, columnPublic);
            codecConfig.addPropertyColumn(property, direction, columnConfig);
        }
        //
        return codecConfig;
    }

    private static List<Element> getChildElements(Element parent) {
        //
        List<Element> childElements = new ArrayList<Element>();
        //
        NodeList nodeList = parent.getChildNodes();
        for(int i=0; i<nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(node.getNodeType()==Node.ELEMENT_NODE) {
                childElements.add((Element) node);
            }
        }
        //
        return childElements;
    }

    private static Element getChildElementByTagName(Element parent, String name) {
        //
        List<Element> childElements = getChildElements(parent);
        for(Element childElement : childElements) {
            //
            if(name.equals(childElement.getTagName())) {
                return childElement;
            }
        }
        //
        return null;
    }
}
