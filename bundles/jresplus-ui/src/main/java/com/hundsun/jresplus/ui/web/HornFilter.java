/*
 * 修改记录
 * 修改时间		修改人		修改记录
 * -------------------------------------------------------------------------------
 * 2014-3-25	XIE			取消运行期压缩合并静态资源的机制，变量上兼容
 */
package com.hundsun.jresplus.ui.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hundsun.jresplus.base.dict.DictManager;

/**
 * 
 * @author Leo
 * 
 */
@Component("hornFilter")
public class HornFilter extends OncePerRequestFilter implements Filter,
		InitializingBean {
	public static final String REQUEST_REMOTE_HOST_MDC_KEY = "req.remoteHost";
	public static final String REQUEST_USER_AGENT_MDC_KEY = "req.userAgent";
	public static final String REQUEST_REQUEST_URI = "req.requestURI";
	public static final String REQUEST_QUERY_STRING = "req.queryString";
	public static final String REQUEST_PARAMETER="req.parameter";
	public static final String REQUEST_REQUEST_URL = "req.requestURL";
	public static final String REQUEST_X_FORWARDED_FOR = "req.xForwardedFor";
	private final static Logger log = LoggerFactory.getLogger(HornFilter.class);
	private final static ThreadLocal<HttpServletRequest> REQUEST = new ThreadLocal<HttpServletRequest>();

	private List<String> headStyleLinks = new ArrayList<String>();

	private List<String> headScriptLinks = new ArrayList<String>();

	@Autowired
	private DictManager dictManager;

	@Value("${response.out.charset}")
	private String outCharset;
	@Value("${horn.ui.title}")
	private String title;
	@Value("${horn.ui.theme}")
	private String theme = "horn";
	@Value("${system.dev.mode}")
	private boolean devMode = false;
	@Value("${horn.ui.vaild.return.param.name}")
	private String validSourceUrlField="sourceurl";
	private boolean emptyParamsCheck = true;
	
	@Value("${request.params.emptycheck}")
	public void setEmptyParamsCheck(String emptyParamsCheck) {
		
		if(StringUtils.isNotBlank(emptyParamsCheck)&& emptyParamsCheck.indexOf("${")== -1){
			  this.emptyParamsCheck = Boolean.valueOf(emptyParamsCheck);
		}else{
			this.emptyParamsCheck = true;
		}
		log.debug("RequestParam[{emptyParamsCheck}][{}] in horn ui filter...",this.emptyParamsCheck);
	}

	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.debug("Request[{}][{}] in Horn UI filter...", request.getMethod(),
				request.getServletPath());
		request.setAttribute("outCharset", outCharset);
		request.setAttribute("title", title);
		request.setAttribute("theme", theme);
		request.setAttribute("devMode", devMode);
		request.setAttribute("initHeadStyleLinks", headStyleLinks);
		request.setAttribute("initHeadScriptLinks", headScriptLinks);
		request.setAttribute("validSourceUrlField", validSourceUrlField);
		request.setAttribute("horn", new HornUtils(request, dictManager));
		request.setAttribute("emptyParamcheck", emptyParamsCheck);
		if (dictManager != null) {
			request.setAttribute("dictManager", dictManager);
		}
		setMDC(request);
		request = new HornHttpServletRequest(request);
		REQUEST.set(request);
		try {
			filterChain.doFilter(request, response);
		} finally {
			REQUEST.remove();
			clearMDC();
		}
	}

	private void setMDC(HttpServletRequest request){
		String requestURI = request.getRequestURI();
		MDC.put(REQUEST_REQUEST_URI, requestURI);
		StringBuffer sbuffer=request.getRequestURL();
		if(sbuffer!=null){
			MDC.put(REQUEST_REQUEST_URL, sbuffer.toString());
		}
		String queryString = request.getQueryString();
		if(queryString!=null){
			MDC.put(REQUEST_QUERY_STRING, queryString);
		}
		String userAgent = request.getHeader("User-Agent");
		if(userAgent!=null){
			MDC.put(REQUEST_USER_AGENT_MDC_KEY, userAgent);
		}
		String forward = request.getHeader("X-Forwarded-For");
		if(forward!=null){
			MDC.put(REQUEST_X_FORWARDED_FOR, forward);
		}
		String remoteHost = request.getRemoteHost();
		if(remoteHost!=null){
			MDC.put(REQUEST_REMOTE_HOST_MDC_KEY, remoteHost);
		}
		if(log.isDebugEnabled()){
			Enumeration<String> pnames=request.getParameterNames();
			StringBuffer parambuffer=new StringBuffer("{");
			boolean flag=false;
			while (pnames.hasMoreElements()) {
				if(flag){
					parambuffer.append(",");
				}
				flag=true;
				String key = pnames.nextElement();
				parambuffer.append(key).append(":").append(request.getParameter(key));
			}
			parambuffer.append("}");
			MDC.put(REQUEST_PARAMETER, parambuffer.toString());
		}
		
	}

	private void clearMDC(){
		MDC.remove(REQUEST_REQUEST_URI);
		MDC.remove(REQUEST_REQUEST_URL);
		MDC.remove(REQUEST_QUERY_STRING);
		MDC.remove(REQUEST_USER_AGENT_MDC_KEY);
		MDC.remove(REQUEST_X_FORWARDED_FOR);
		MDC.remove(REQUEST_REMOTE_HOST_MDC_KEY);
	}
	public static HttpServletRequest getRequest() {
		return REQUEST.get();
	}

	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		if(StringUtils.isBlank(theme)||theme.indexOf("${")>-1){
			theme="horn";
		}
		if(title ==null||title.indexOf("${")>-1){
			title="系统";
		}
		if(StringUtils.isBlank(validSourceUrlField)||validSourceUrlField.indexOf("${")>-1){
			validSourceUrlField="sourceurl";
		}
		headStyleLinks.add("/components/components.css");
		headScriptLinks.add("/components/components.js");
		log.info("Horn Ui properties [horn.ui.title]:[{}]",title);
		log.info("Horn Ui properties [horn.ui.theme]:[{}]",theme);

	}

}
