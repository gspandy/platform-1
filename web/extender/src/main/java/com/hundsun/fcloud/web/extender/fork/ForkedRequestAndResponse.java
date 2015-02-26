package com.hundsun.fcloud.web.extender.fork;

/**
 * Created by Gavin Hu on 2015/2/7.
 */
public class ForkedRequestAndResponse {

    private String forkedName;

    private ForkedHttpServletRequest forkedRequest;

    private ForkedHttpServletResponse forkedResponse;

    public ForkedRequestAndResponse(String forkedName, ForkedHttpServletRequest forkedRequest, ForkedHttpServletResponse forkedResponse) {
        this.forkedName = forkedName;
        this.forkedRequest = forkedRequest;
        this.forkedResponse = forkedResponse;
    }

    public String getForkedName() {
        return forkedName;
    }

    public ForkedHttpServletRequest getForkedRequest() {
        return forkedRequest;
    }

    public ForkedHttpServletResponse getForkedResponse() {
        return forkedResponse;
    }
}
