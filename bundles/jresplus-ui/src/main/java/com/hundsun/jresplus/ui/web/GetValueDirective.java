package com.hundsun.jresplus.ui.web;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author LeoHu copy sagahl
 * 
 */
public class GetValueDirective extends Directive {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 */
	@Override
	public String getName() {
		return "getValue";
	}

	/**
	 */
	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		Node bodyNode = getBodyNode(context, node);
		String key = (String) bodyNode.value(context);
		if (null != context.get(key)) {
			writer.write(String.valueOf(context.get(key)));
		}
		return true;
	}

	protected Node getBodyNode(InternalContextAdapter context, Node node) {
		int children = node.jjtGetNumChildren();
		if (children == 1) {
			return node.jjtGetChild(0);
		} else {
			return node.jjtGetChild(children - 1);
		}
	}

}
