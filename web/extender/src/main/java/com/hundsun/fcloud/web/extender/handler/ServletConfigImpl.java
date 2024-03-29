/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hundsun.fcloud.web.extender.handler;

import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import java.util.Enumeration;
import java.util.Collections;
import java.util.Map;

public final class ServletConfigImpl implements ServletConfig {
    private final String name;
    private final ServletContext context;
    private final Map<String, String> initParams;

    public ServletConfigImpl(String name, ServletContext context, Map<String, String> initParams) {
        this.name = name;
        this.context = context;
        this.initParams = initParams;
    }

    public String getServletName() {
        return this.name;
    }

    public ServletContext getServletContext() {
        return this.context;
    }

    public String getInitParameter(String name) {
        return this.initParams.get(name);
    }

    public Enumeration getInitParameterNames() {
        return Collections.enumeration(this.initParams.keySet());
    }
}