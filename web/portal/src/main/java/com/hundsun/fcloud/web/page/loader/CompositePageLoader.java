package com.hundsun.fcloud.web.page.loader;

import com.hundsun.fcloud.web.page.config.PageConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Gavin Hu on 2014/11/25.
 */
public class CompositePageLoader implements PageLoader {

    private List<PageLoader> pageLoaders = new ArrayList<PageLoader>();

    public CompositePageLoader(PageLoader...pageLoaders) {
        this.pageLoaders = Arrays.asList(pageLoaders);
    }

    @Override
    public PageConfig load(String path, Map<String, String> params) {
        //
        for(PageLoader pageLoader : pageLoaders) {
            try {
                return pageLoader.load(path, params);
            } catch (PageLoaderException e) {
                // ignore exception
            }
        }
        //
        throw new PageLoaderException(String.format("页面配置文件 %s 未找到！", path));
    }
}
