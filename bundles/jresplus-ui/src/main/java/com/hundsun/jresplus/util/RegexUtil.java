package com.hundsun.jresplus.util;

public class RegexUtil {

	/**
	 * 身份证号校验
	 */
	public static final String IDCARD = "^$|^[1-9][0-9]{16}[0-9xX]$";
	/**
	 * 日期
	 */
	public static final String DATE = "^$|^\\d{4}\\d{1,2}\\d{1,2}$";
	/**
	 * 整数
	 */
	public static final String INTEGER = "^$|^-?[1-9]\\d*$";
	/**
	 * 正整数
	 */
	public static final String INTEGER1 = "^$|^[1-9]\\d*$";
	/**
	 * 负整数
	 */
	public static final String INTEGER2 = "^$|^-[1-9]\\d*$";
	/**
	 * 数字
	 */
	public static final String NUM = "^$|^([+-]?)\\d*\\.?\\d*$";
	/**
	 * 正数（正整数 + 0）
	 */
	public static final String NUM1 = "^$|^[1-9]\\d*|0$";
	/**
	 * 负数（负整数 + 0）
	 */
	public static final String NUM2 = "^$|^-[1-9]\\d*|0$";
	/**
	 * 浮点数
	 */
	public static final String DECIMAL = "^$|^([+-]?)\\d*\\.\\d+$";
	/**
	 * 正浮点数
	 */
	public static final String DECIMAL1 = "^$|^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$";
	/**
	 * 负浮点数
	 */
	public static final String DECIMAL2 = "^$|^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$";
	/**
	 * 浮点数
	 */
	public static final String DECIMAL3 = "^$|^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$";
	/**
	 * 非负浮点数（正浮点数 + 0）
	 */
	public static final String DECIMAL4 = "^$|^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$";
	/**
	 * 非正浮点数（负浮点数 + 0）
	 */
	public static final String DECIMAL5 = "^$|^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$";
	/**
	 * 邮件
	 */
	public static final String EMAIL = "^$|^(\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+){0,1}$";
	/**
	 * 颜色
	 */
	public static final String COLOR = "^$|^[a-fA-F0-9]{6}$";
	/**
	 * URL
	 */
	public static final String URL = "^$|^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";
	/**
	 * 仅中文
	 */
	public static final String CHINESE = "^$|^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
	/**
	 * 仅ACSII字符
	 */
	public static final String ASCII = "^$|^[\\x00-\\xFF]+$";
	/**
	 * 邮编
	 */
	public static final String ZIPCODE = "^$|^\\d{6}$";
	/**
	 * 手机
	 */
	public static final String MOBILE = "^$|^(13|15|18)[0-9]{9}$";
	/**
	 * IP地址
	 */
	public static final String IP4 = "^$|^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";
	/**
	 * 非空
	 */
	public static final String NOTEMPTY = "^$|^\\S+$";
	/**
	 * 图片
	 */
	public static final String PICTURE = "^$|(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$";
	/**
	 * 压缩文件
	 */
	public static final String RAR = "^$|(.*)\\.(rar|zip|7zip|tgz)$";
	/**
	 * QQ号码
	 */
	public static final String QQ = "^$|^[1-9]*[1-9][0-9]*$";
	/**
	 * 电话号码的函数(包括验证国内区号,国际区号,分机号)
	 */
	public static final String TEL = "^$|^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$";
	/**
	 * 用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
	 */
	public static final String USERNAME = "^$|^\\w+$";
	/**
	 * 字母
	 */
	public static final String LETTER = "^$|^[A-Za-z]+$";
	/**
	 * 大写字母
	 */
	public static final String LETTER_U = "^$|^[A-Z]+$";
	/**
	 * 小写字母
	 */
	public static final String LETTER_L = "^$|^[a-z]+$";
	
}
