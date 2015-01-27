/*
 * 源程序名称: PipelineTaskWrapper.java 
 * 软件著作权: 恒生电子股份有限公司 版权所有
 * 模块名称：TODO(这里注明模块名称)
 * 
 */

package com.hundsun.jresplus.ui.contain;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringEscapeUtils;

import com.alibaba.fastjson.JSON;
import com.hundsun.jresplus.common.util.ArrayUtil;
import com.hundsun.jresplus.web.contain.pipeline.PipelineTask;

public class PipelineTaskWrapper {
	private PipelineTask task;

	public PipelineTaskWrapper(String view, String id) {
		task = new PipelineTask(view, id);
	}

	public PipelineTask getTask() {
		return task;
	}

	public void write(Writer writer, String result) throws IOException {
		writer.write("{\"html\":\"");
		StringEscapeUtils.escapeJavaScript(writer, result);
		writer.write("\",\"id\":\"");
		writer.write(task.getId());
		writer.write("\",\"css\":");
		if (ArrayUtil.isEmpty(task.getCss())) {
			writer.write("[]");
		} else {
			writer.write(JSON.toJSONString(task.getCss()));
		}
		writer.write(",\"js\":");
		if (ArrayUtil.isEmpty(task.getJs())) {
			writer.write("[]");
		} else {
			writer.write(JSON.toJSONString(task.getJs()));
		}
		writer.write(",\"jsCode\":\"");
		if (task.getJsCode().length() < 1) {
			writer.write("");
		} else {
			writer.write(task.getJsCode().toString());
		}
		writer.write("\"}");
	}

}
