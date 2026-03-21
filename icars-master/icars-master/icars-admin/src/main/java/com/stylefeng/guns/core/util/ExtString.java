package com.stylefeng.guns.core.util;

import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 * @author janage
 *
 */
public class ExtString {
	private static int randomSEQ = 0;
	public static String ClobToString(Object clob) throws Exception {
		if(clob instanceof Clob){
			Clob clob2=(Clob)clob;
			String reString = "";
			Reader is = clob2.getCharacterStream();// 得到流
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
				sb.append(s);
				s = br.readLine();
			}
			reString = sb.toString();
			return reString;
		}else if(clob instanceof  String){
			return (String)clob;
		}else {
			return "";
		}

	}
	/**
	 * 提供替换字符串的功能,区分大小写
	 * 
	 * @param
	 *
	 * @param
	 *            sStr 为被替换的子字符串
	 * @param
	 *            sRepStr 为替换进去的子字符串
	 * @return 无
	 */
	public static void replace(StringBuffer sb, String sStr, String sRepStr) {
		try {
			if ((sb == null) || (sStr == null) || (sRepStr == null))
				return;
			if ((sb.length() == 0) || (sStr.length() == 0))
				return;
			int iStartIndex = 0;
			int iLen = sb.length();
			int iLen2 = sStr.length();
			while (iStartIndex < iLen) {
				if (sb.substring(iStartIndex, iLen2 + iStartIndex).equals(sStr)) {
					sb.replace(iStartIndex, iLen2 + iStartIndex, sRepStr);
					iLen = sb.length();
					iStartIndex = iStartIndex + sRepStr.length();
				} else
					iStartIndex++;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 提供替换字符串的功能,区分大小写
	 * 
	 * @param
	 *            sSrcStr 为要被处理的字符串
	 * @param
	 *            sStr 为被替换的子字符串
	 * @param
	 *            sRepStr 为替换进去的子字符串
	 * @return String 替换后的字符串,出现异常返回原字符串
	 */
	public static String replace(String sSrcStr, String sStr, String sRepStr) {
		try {
			if ((sSrcStr == null) || (sStr == null) || (sRepStr == null))
				return sSrcStr;
			if ((sSrcStr.length() == 0) || (sStr.length() == 0))
				return sSrcStr;
			StringBuffer sb = new StringBuffer(sSrcStr);
			replace(sb, sStr, sRepStr);
			return new String(sb);
		} catch (Exception e) {
			return sSrcStr;
		}
	}

	/**
	 * 提供字符串分割的功能,区分大小写
	 * 
	 * @param
	 *            sStr 为要被处理的源字符串
	 * @param
	 *            sSplitStr 分割串或结束串
	 * @param
	 *            sEscStr 为转义字符串
	 * @param
	 *            iSplitnum 为要分割的个数
	 * @param
	 *            flag flag=1时: 当iSplitnum>0时，严格按其要
	 *            求分割的个数分割，即最后被分割出来的可能还含有分割符（它们将被忽略） 当iSplitnum
	 *            <=0时,sSplitStr被认为是结束符，即只有sSplitStr左边的有效。 flag=0表示sSplitStr为结束符
	 *            当iSplitnum <0时,sSplitStr被认为是结束符，即只有sSplitStr左边的有效。
	 *            当iSplitnum>=0时,sSplitStr被认为分割符，即只有sSplitStr左右边都有效。(但当sSplitStr在最后时候，右边无效)
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr, String sSplitStr, String sEscStr,
			int iSplitnum, int flag) {
		try {
			String[] aResult = null;
			Vector vResult = null;

			if (sSplitStr == null)
				sSplitStr = "";
			if (sSplitStr.length() == 0) {
				aResult = new String[1];
				aResult[0] = sStr;
				return aResult;
			}
			if (iSplitnum <= 0)
				iSplitnum = 0;
			if (sEscStr == null)
				sEscStr = "";

			if (iSplitnum > 0)
				aResult = new String[iSplitnum];
			else
				vResult = new Vector();

			int iNum = 0;
			int index = 0;
			int preindex = 0;
			int len = sStr.length();
			int iSplitlen = sSplitStr.length();
			int iEsclen = sEscStr.length();
			String sNosplit = sEscStr + sSplitStr;
			String sTemp = null;
			String sTemp2 = null;

			while (index < len) {
				try {
					sTemp = sStr.substring(index, iSplitlen + index);
				} catch (Exception e) {
					sTemp = "";
				}

				if (sTemp.equals(sSplitStr)) {
					sTemp2 = null;
					try {
						if (iEsclen == 0)
							sTemp2 = sStr.substring(preindex, index);
						else {
							if (index - iEsclen < 0)
								sTemp2 = sStr.substring(preindex, index);
							else if (!sStr.substring(index - iEsclen,
									iSplitlen + index).equals(sNosplit))
								sTemp2 = sStr.substring(preindex, index);
						}
					} catch (Exception e) {
						sTemp2 = "";
					}

					if (sTemp2 != null) {
						if (iEsclen > 0)
							sTemp2 = replace(sTemp2, sNosplit, sSplitStr);
						if (iSplitnum > 0)
							aResult[iNum] = sTemp2;
						else
							vResult.add(iNum, sTemp2);
						iNum++;
						if ((iNum >= iSplitnum) && (iSplitnum > 0))
							break;
						index = iSplitlen + index;
						preindex = index;

						if ((flag == 1) && (iSplitnum - iNum == 1)
								&& (index < len)) {
							sTemp2 = sStr.substring(index);
							if (iEsclen > 0)
								sTemp2 = replace(sTemp2, sNosplit, sSplitStr);
							if (iSplitnum > 0)
								aResult[iNum] = sTemp2;
							else
								vResult.add(iNum, sTemp2);
							iNum++;
							break;
						}
						continue;
					}
				}
				index++;

			}

			if (((flag == 0) && (preindex < index) && (iNum < iSplitnum))
					|| ((iSplitnum == 0) && (preindex < index))) {
				sTemp2 = sStr.substring(preindex, index);
				if (iEsclen > 0)
					sTemp2 = replace(sTemp2, sNosplit, sSplitStr);
				if (iSplitnum > 0)
					aResult[iNum] = sTemp2;
				else
					vResult.add(iNum, sTemp2);
				iNum++;
			}

			if (iNum == 0) {
				aResult = new String[1];
				aResult[0] = sStr;
				return aResult;
			}

			if (iNum < iSplitnum)
				for (int i = iNum; i < iSplitnum; i++)
					if (iSplitnum == 0)
						vResult.add(i, "");
					else
						aResult[i] = "";

			if (iSplitnum == 0) {
				aResult = new String[vResult.size()];
				for (int i = 0; i < aResult.length; i++)
					aResult[i] = (String) (vResult.elementAt(i));
			}

			return aResult;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提供字符串分割的功能,区分大小写
	 * 
	 * @param
	 *            sStr 为要被处理的源字符串
	 * @param
	 *            sSplitStr 结束字符串
	 * @param
	 *            sEscStr 为转义字符串
	 * @param
	 *            iSplitnum 为要分割的个数
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr, String sSplitStr, String sEscStr,
			int iSplitnum) {
		try {
			return split(sStr, sSplitStr, sEscStr, iSplitnum, 0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提供分割字符串的功能,区分大小写
	 * 
	 * @param
	 *            sStr 为要被处理的源字符串
	 * @param
	 *            sSplitStr 分割串
	 * @param
	 *            sEscStr 为转义字符串
	 * @return 按要求分割后的字符数组，为null表示失败
	 */
	public static String[] split(String sStr, String sSplitStr, String sEscStr) {
		try {
			return split(sStr, sSplitStr, sEscStr, 0, 0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提供分割字符串的功能,区分大小写
	 * 
	 * @param
	 *            sStr 为要被处理的源字符串
	 * @param
	 *            sFieldStr 字段结束符
	 * @param
	 *            sLineStr 行结束符
	 * @param
	 *            sEscStr 为转义字符串
	 * @param
	 *            iLinenum 行数
	 * @param
	 *            iFieldnum 列数
	 * @return 按要求分割后的二维字符数组，为null表示失败
	 */
	public static String[][] split(String sStr, String sFieldStr,
			String sLineStr, String sEscStr, int iLinenum, int iFieldnum) {
		try {
			String[][] aaResult = new String[iLinenum][iFieldnum];

			String[] aTemp = split(sStr, sLineStr, sEscStr, iLinenum);
			if (aTemp.length != aaResult.length)
				return null;

			for (int i = 0; i < aaResult.length; i++)
				aaResult[i] = split(aTemp[i], sFieldStr, sEscStr, iFieldnum);

			if (aaResult[0].length != iFieldnum)
				return null;

			return aaResult;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 判断字符串是否为null，若是，返回""，否则返回原字符串
	 * 
	 * @param str
	 *            需要判断的字符串.
	 * @return 返回去除前后空格的字符串,或都空串"".
	 */
	public static String turnStr(String str) {
		if (str == null)
			return "";
		return str.trim();
	}

	/**
	 * 生成随机密码文本。
	 * 
	 * @param iLength
	 *            要生成密码的长度，它不能小于3位。
	 * @return 生成的随机密码。
	 */
	public static String getRandomPassword(int iLength) {
		try {
			String[] m_srcStr = new String[] {
					"abcdefghijklmnopqrstuvwxyz67890",
					"ABCDEFGHIJKLMNOPQRSTUVWXYZ12345",
					"~!@#$%^&*()_+{}|\":?><`-=[]\';/.," };

			int len = 6;
			if (iLength >= 3)
				len = iLength;
			Random m_rnd = new Random();
			byte[] m_b = new byte[len];
			m_rnd.nextBytes(m_b);
			String m_pwdStr = "";
			for (int i = 0; i < len; i++) {
				int startIdx = Math.abs((int) m_b[i] % 31);
				m_pwdStr += m_srcStr[i % 3].substring(startIdx, startIdx + 1);
			}

			return m_pwdStr;
		} catch (Exception ex) {
//			LogUtil.info("生成随机密码时发生异常："+ex.getMessage());

			throw new RuntimeException(ex);
		}
	}
	
	public static String getRandomString(int length, boolean hasSpecialChar) {
		try {
			String[] m_srcStr = new String[] {
					"abcdefghijklmnopqrstuvwxyz67890",
					"ABCDEFGHIJKLMNOPQRSTUVWXYZ12345",
					"~!@#$%^&*()_+{}|\":?><`-=[]\';/.," };

			int len = 6;
			if (length >= 3)
				len = length;
			
			// 生成唯一的时间点作为生成随机字符的基本信息。
			long seed = System.currentTimeMillis();
			seed = getHashCodeValue(seed);
			seed = seed * 10000 + getRandomSeq();
			
			Random m_rnd = new Random(seed);
			byte[] m_b = new byte[len];
			m_rnd.nextBytes(m_b);
			String m_pwdStr = "";
			int groupCount = 2;
			if ( hasSpecialChar ) {
				groupCount = 3;
			}
			for (int i = 0; i < len; i++) {
				int startIdx = Math.abs((int) m_b[i] % 31);
				m_pwdStr += m_srcStr[i % groupCount].substring(startIdx, startIdx + 1);
			}

			return m_pwdStr;
		} catch (Exception ex) {
//			LogUtil.info("生成随机密码时发生异常："+ex);

			throw new RuntimeException(ex);
		}
	}

	private static long getHashCodeValue(final long seed) {
		try {
			int hashCode = Thread.currentThread().hashCode();
			StringBuffer str = new StringBuffer("1");
			for ( int i=0; i<Integer.toString(hashCode).length(); i++) {
				str.append("0");
			}
			return seed * Integer.parseInt(str.toString()) + hashCode;
		} catch (NumberFormatException e) {
			return seed;
		}
	}

	private static synchronized int getRandomSeq() {
		if ( randomSEQ > 9999 ) randomSEQ = 0;
		
		return randomSEQ++;
	}



	public static String getHTMLString(String str) {
		String ret = replace(str, "\"", "&quot;");
		ret = replace(ret, "<", "&lt;");
		ret = replace(ret, ">", "&gt;");
		ret = replace(ret, " ", "&nbsp;");
		ret = replace(ret, "'", "&#39;");
		ret = replace(ret, "\r\n", "\n");
		ret = replace(ret, "\n", "<br>");
		

		return ret;
	}

	public static String toBASE64String(String sURL) {
		try {
			byte[] aUrl = sURL.getBytes();
			BASE64Encoder encoder = new BASE64Encoder();
			String url = encoder.encode(aUrl);

			return url;
		} catch (Exception ex) {
		}

		return null;
	}

	public static String fromBASE64String(String sURL) {
		try {
			sun.misc.BASE64Decoder encoder = new sun.misc.BASE64Decoder();
			byte[] aUrl = encoder.decodeBuffer(sURL);

			return new String(aUrl);
		} catch (Exception ex) {
		}

		return null;
	}

	public static String fromDate(Date dObj) {
		try {
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return f1.format(dObj);
		} catch (Exception e) {
//			LogUtil.warn(e.getMessage());
		}

		return null;
	}

	public static Date toDate(String sDate) {
		try {
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return f1.parse(sDate);
		} catch (Exception e) {
//			LogUtil.warn(e.getMessage());
		}

		return null;
	}


	/**
	 * 检查字段串是否为空，注，它有去除前后的空字符。
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals(""))
			return true;

		return false;
	}

	/**
	 * 检查指定的字符串是否是数字类型，空字符串不是数字。
	 * 
	 * @param str
	 *            待检查的字符串
	 * @return 如果全是数字，则返回true，否则返回false。
	 */
	public static boolean isNumeric(final String str) {
		if (isEmpty(str)) {
			return false;
		}
		
		final char[] numbers = str.toCharArray();
		for (int x = 0; x < numbers.length; x++) {
			final char c = numbers[x];
			if ((c >= '0') && (c <= '9')) {
				continue;
			}
			
			return false;
		}
		return true;
	}
	
	
	/**
	 * 将长整形转换为整形。
	 * 
	 * @param str
	 * @return
	 */
	public static int getInt(final String str) {
		if ( isNumeric(str)) {
			try {
				return Integer.parseInt(str);
			} catch (NumberFormatException e) {
			}
		}
		
		return -1;
	}
	
	/**
	 * 将字符串转换为长整形。
	 * 
	 * @param str
	 * @return
	 */
	public static long getLong(final String str) {
		if ( isNumeric(str)) {
			try {
				return Long.parseLong(str);
			} catch (NumberFormatException e) {
			}
		}
		
		return -1;
	}
	
	public static String getRandomCode(int iLength){
	    try {
	        String[] m_srcStr = new String[] { "012345678998765432", "123456789009876543" };

	        int len = 6;
	        if (iLength >= 3)
	            len = iLength;
	        Random m_rnd = new Random();
	        byte[] m_b = new byte[len];
	        m_rnd.nextBytes(m_b);
	        String m_pwdStr = "";
	        for (int i = 0; i < len; i++) {
	            int startIdx = Math.abs((int) m_b[i] % 18);
	            m_pwdStr += m_srcStr[i % 2].substring(startIdx, startIdx + 1);
	        }

	        return m_pwdStr;
	    } catch (Exception ex) {
//	    	LogUtil.warn("生成随机密码时发生异常："+ex);

	        throw new RuntimeException(ex);
	    }
	}
	
	
	/**
	 * 脱敏
	 * @param accountId
	 * @return
	 */
	public static String getSecretStr(String accountId){
		String str ="";
		if (accountId.length()>=11) {
			String substr = accountId.substring(0,3);
			String subend = accountId.substring(7,accountId.length());
			str = substr+"****"+subend;
		}else if(accountId.length()<5){
			for (int i = 0; i <accountId.length() ; i++) {
				str+="*";
			}
		}else if(accountId.length()<8&&accountId.length()>4){
			String substr = accountId.substring(0,(accountId.length()-4));
			str = substr+"****";
		}else if(accountId.length()<11&&accountId.length()>7){
			String substr = accountId.substring(0,3);
			String subend = accountId.substring(8,accountId.length());
			str = substr+"****"+subend;
		}
		return str;
	}
	
}