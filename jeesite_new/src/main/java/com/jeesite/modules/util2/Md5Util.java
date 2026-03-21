package com.jeesite.modules.util2;

import java.security.MessageDigest;

public final class Md5Util {
	private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static Md5Util instance = null;
	private Md5Util() { 		
	}
	public synchronized static Md5Util getInstance() {
		if(instance==null){
			instance=new Md5Util();
		}
		return instance;
	}
	public String getShortToken(String arg0) {
		return encoder(arg0).substring(8,24);
	}
	public String getLongToken(String arg0) {
		return encoder(arg0).toString();
	}
	private StringBuffer encoder(String arg){
		if(arg==null){
			arg="";
		}
		MessageDigest md5 = null;
		try {
			md5=MessageDigest.getInstance("MD5");
			md5.update(arg.getBytes("UTF8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toHex(md5.digest());
	}
	private StringBuffer toHex(byte[] bytes) {
		StringBuffer str = new StringBuffer(32);
		int length=bytes.length;
		for (int i = 0; i < length; i++) {
			str.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
			str.append(hexDigits[bytes[i] & 0x0f]);
		}
		bytes=null;
		return str;
	}
	public static void main(String a[]){

		Md5Util instance = Md5Util.getInstance();
		String longToken = instance.getLongToken("123456");
		System.out.println(longToken);

	}
}