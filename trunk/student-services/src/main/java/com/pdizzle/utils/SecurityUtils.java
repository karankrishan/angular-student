package com.pdizzle.utils;

import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SecurityUtils {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(SecurityUtils.class);
	private static final String PWD = "Ab3nder!";
	
	private static BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

	static {
		textEncryptor.setPassword(PWD);
	}
	
	public static final String unencrypt(String s) {
		return textEncryptor.decrypt(s);
	}
	
	public static String encyrpt(String s) {
		return textEncryptor.encrypt(s);
	}
	
	public static void main(String[] args){
		System.out.println(encyrpt("test"));
	}
}
