package com.hundsun.jresplus.ui.tipmessage;

/**
 * UI中的页面提示对象
 * 
 * @author Leo
 * 
 */
public class TipMessage {

	/**
	 * 普通的信息
	 */
	public final static char INFO = 'C';

	/**
	 * 报警的信息
	 */
	public final static char WARNING = 'W';

	/**
	 * 成功的信息
	 */
	public final static char SUCCESS = 'S';

	private String message;

	private char type = INFO;

	public TipMessage() {

	}

	public TipMessage(String message) {
		this.type = INFO;
		this.message = message;
	}

	public TipMessage(char type, String message) {
		this.type = type;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

}
