/*
 * 修改记录：
 * 日期			修改人	备注
 * ============================================================================
 * 2014-9-18	XIE		兼容1.0.6之后使用PipelineTask代替AsyncContainTask
 * ============================================================================
 */
package com.hundsun.jresplus.ui.contain;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.hundsun.jresplus.beans.config.BeanOverride;
import com.hundsun.jresplus.common.util.StringUtil;
import com.hundsun.jresplus.web.contain.ContainFilter;
import com.hundsun.jresplus.web.contain.ResponseStringWriterWrapper;
import com.hundsun.jresplus.web.contain.async.AsynchronContainTask;
import com.hundsun.jresplus.web.contain.async.AsynchronousContain;

/**
 * 
 * @author LeoHu copy by sagahl
 * 
 */
@BeanOverride(override = ContainFilter.class)
public class HornContainFilter extends ContainFilter implements Filter,
		ServletContextAware {
	Logger log = LoggerFactory.getLogger(HornContainFilter.class);
	public static final String KEY = "pagelet";
	private boolean isSupportPipelineTask = false;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (StringUtil.isEmpty(request.getParameter(KEY))) {
			super.doFilter(request, response, chain);
			return;
		}

		String id = request.getParameter(KEY);
		request.setAttribute(KEY, id);
		if (log.isDebugEnabled()) {
			log.debug("Request[{}] is a pagelet[{}] request.",
					((HttpServletRequest) request).getServletPath(), id);
		}
		ResponseStringWriterWrapper responseWrapper = new ResponseStringWriterWrapper(
				(HttpServletResponse) response);
		AsynchronContainTask task = new AsynchronContainTask(id,
				responseWrapper);
		PipelineTaskWrapper taskWrapper = null;
		if (isSupportPipelineTask) {
			taskWrapper = new PipelineTaskWrapper(
					((HttpServletRequest) request).getServletPath(), id);
			request.setAttribute(AsynchronContainTask.AsynchronContainTaskName,
					taskWrapper.getTask());
		} else {
			task = new AsynchronContainTask(id, responseWrapper);
			request.setAttribute(AsynchronContainTask.AsynchronContainTaskName,
					task);
		}

		AsynchronousContain.setAsyncConext();
		try {
			super.doFilter(request, responseWrapper, chain);
		} finally {
			try {
				if (isSupportPipelineTask) {
					taskWrapper.write(response.getWriter(), responseWrapper
							.getStringWriter().toString());
				} else {
					task.writeTo(response.getWriter());
				}

			} finally {
				AsynchronousContain.removeAsyncConext();
			}
		}
	}

	public void afterPropertiesSet() throws ServletException {
		/** 兼容1.0.6之前的mvc处理 */
		isSupportPipelineTask = isSupportPipelineTask();
		log.info("support pipelineTask[{}]", isSupportPipelineTask);
		super.afterPropertiesSet();
	}

	private boolean isSupportPipelineTask() {
		/** 兼容1.0.6之前的mvc处理，如果是1.0.6版本之后则PipelineTask类是存在的 */
		try {
			Class.forName("com.hundsun.jresplus.web.contain.pipeline.PipelineTask");
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
