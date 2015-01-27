package com.hundsun.jresplus.ui.page;

/**
 * 分页对象
 * 
 * @author Leo
 * 修改记录：
 * 2014-04-21           zhangsu   BUG #6776 pagebar的下一页按钮，在初次渲染的时候点击无效，到第二页需要点击两次
 */
public class Page {

	private int pageNo = 1;   //BUG #6776

	private int pageSize = 20;

	private int count;

	private int pages;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
		if (count != 0 && pageSize != 0) {
			if ((count % pageSize) == 0) {
				this.pages = count / pageSize;
			} else {
				this.pages = count / pageSize + 1;
			}
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

}
