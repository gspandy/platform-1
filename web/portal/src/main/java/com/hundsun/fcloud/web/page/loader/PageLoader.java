package com.hundsun.fcloud.web.page.loader;


import com.hundsun.fcloud.web.page.config.PageConfig;

import java.util.Map;

/**
 * Created by Gavin Hu on 2014/11/25.
 */
public interface PageLoader {

    PageConfig load(String path, Map<String, String> params);

}
