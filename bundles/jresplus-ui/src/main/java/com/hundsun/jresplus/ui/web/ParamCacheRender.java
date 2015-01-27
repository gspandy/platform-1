package com.hundsun.jresplus.ui.web;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Renderable;

import com.alibaba.fastjson.JSONObject;
import com.hundsun.jresplus.web.velocity.eventhandler.DirectOutput;

public class ParamCacheRender implements DirectOutput, Renderable {


	private HttpServletRequest request;
	public ParamCacheRender(HttpServletRequest request) {
		this.request = request;
	}

	public boolean render(InternalContextAdapter adapter, Writer writer)
			throws IOException, MethodInvocationException,
			ParseErrorException, ResourceNotFoundException {
		if (request.getAttribute("paramCaches") == null) {
			writer.write("{}");
			return true;
		}
		@SuppressWarnings("unchecked")
		Set<ParamCache> paramCaches = (Set<ParamCache>) request
				.getAttribute("paramCaches");

		JSONObject out = new JSONObject();
		for (ParamCache paramCache : paramCaches) {
			out.put(paramCache.getParamId(), paramCache.getParams());
		}
		writer.write(out.toJSONString());
		return true;
	}
}