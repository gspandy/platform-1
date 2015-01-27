package com.hundsun.jresplus.ui.web;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.Renderable;

import com.hundsun.jresplus.web.velocity.eventhandler.DirectOutput;

public class ParamIdRender implements DirectOutput, Renderable {

	private String uuid;

	ParamIdRender(String uuid) {
		this.uuid = uuid;
	}

	public boolean render(InternalContextAdapter adapter, Writer writer)
			throws IOException, MethodInvocationException,
			ParseErrorException, ResourceNotFoundException {
		writer.write("paramCacheId=\"" + uuid + "\"");
		return true;
	}

}