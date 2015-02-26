package com.hundsun.fcloud.web.page.loader;

import com.hundsun.fcloud.web.page.config.PageConfig;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Gavin Hu on 2015/2/26.
 */
public class ServletContextPageLoader extends AbstractPageLoader {

    private ServletContext servletContext;

    public ServletContextPageLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
        super.setPrefix("/WEB-INF/pages/");
        super.setSuffix(".xml");
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public PageConfig load(String path, Map<String, String> params) {

        try {
            String pageLocation = getPageLocation(path);
            InputStream pageSource = servletContext.getResourceAsStream(pageLocation);
            //
            JAXBContext context = JAXBContext.newInstance(PageConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            PageConfig pageConfig = (PageConfig) unmarshaller.unmarshal(pageSource);
            pageConfig.setPath(path);
            //
            if (pageConfig.getParent()!=null) {
                PageConfig parentPageConfig = load(pageConfig.getParent(), params);
                mergePageConfigFromParent(pageConfig, parentPageConfig);
            }
            //
            return pageConfig;
        } catch (Exception e) {
            throw new PageLoaderException("页面配置文件加载异常！", e);
        }
    }

}
