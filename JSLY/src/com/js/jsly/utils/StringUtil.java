package com.js.jsly.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lv on 2016/4/10.
 */
public class StringUtil {

    /**
     * The empty String {@code ""}.
     */
    public static final String EMPTY = "";


    // �ַ�������ö��
    public static enum REGEX_ENUM {
        EMAIL("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"), CHINESE_CHARACTER(
                "[\\u4E00-\\u9FA5]+");
        private String value;


        private REGEX_ENUM(String value) {
            this.value = value;
        }


        public String toString() {
            return this.value;
        }
    };


    /**
     * ����ַ���str�Ƿ�ƥ��������ʽregex
     *
     * @param regex
     * @param str
     * @return
     */
    public static boolean matcherRegex(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * �Ƿ�Ϊ����
     *
     * @param ch
     * @return
     */
    public static boolean isChineseCharacter(char ch) {
        return matcherRegex(REGEX_ENUM.CHINESE_CHARACTER.toString(),
                String.valueOf(ch));
    }


    /**
     * ���ֽڽ�ȡ�ַ���
     *
     * @param str
     *            Ҫ��ȡ���ַ���
     * @param byteLength
     *            ����
     * @return ����ַ���
     */
    public static String subString(String str, int byteLength) {
        if (isBlank(str))
            return EMPTY;
        if (str.getBytes().length <= byteLength)
            return str;
        if (str.length() >= byteLength)
            str = str.substring(0, byteLength);
        int readLen = 0;
        String c = null;
        StringBuffer sb = new StringBuffer(EMPTY);
        for (int i = 0; i < str.length(); i++) {
            c = String.valueOf(str.charAt(i));
            readLen += c.getBytes().length;
            if (readLen > byteLength)
                return sb.toString();
            sb.append(c);
        }
        return sb.toString();
    }


    /**
     * check length (minLength<=str.length<=maxLength)
     *
     * @param str
     *            input String
     * @param minLength
     *            minute length
     * @param maxLength
     *            maximum length
     * @return boolean true: minLength<=str.length<=maxLength false: other
     */
    public static boolean checkLength(String str, int minLength, int maxLength) {
        if (isBlank(str))
            return false;
        int len = str.length();
        if (minLength == 0)
            return len <= maxLength;
        else if (maxLength == 0)
            return len >= minLength;
        else
            return (len >= minLength && len <= maxLength);
    }


    /**
     * decode string by UTF-8
     *
     * @param str
     *            input string
     * @return String decode string
     */
    public static String decodeString(String str) {
        return decodeString(str, "UTF-8");
    }

    /**
     * decode string by the input encoding
     *
     * @param str
     * @param encoding
     * @return
     */
    public static String decodeString(String str, String encoding) {
        if (isBlank(str))
            return EMPTY;
        try {
            return URLDecoder.decode(str.trim(), encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return EMPTY;
    }

    /**
     * decode the string
     *
     * @param str
     * @param encoding
     * @return
     */
    public static String decodeURI(String str) {
        if (isBlank(str))
            return EMPTY;
        try {
            return new String(str.getBytes("ISO8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return EMPTY;
    }

    /**
     * encode String by UTF-8
     *
     * @param str
     *            input string
     * @return String
     */
    public static String encodeString(String str) {
        return encodeString(str, "UTF-8");
    }

    /**
     * encode String by the input encoding
     *
     * @param str
     *            input string
     * @return String encode string
     */
    public static String encodeString(String str, String encoding) {
        if (isBlank(str))
            return EMPTY;
        try {
            return URLEncoder.encode(str.trim(), encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return EMPTY;
    }

    /**
     * get the only string by time
     *
     * @return
     */
    public static String getOnlyString() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * <p>
     * Checks if a CharSequence is whitespace, empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs
     *            the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(cs.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks if a CharSequence is not empty (""), not null and not whitespace
     * only.
     * </p>
     *
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs
     *            the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null and
     *         not whitespace
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtil.isBlank(cs);
    }

    /**
     * parses the specified string as a signed decimal integer value
     *
     * @param str
     *            input string representation of an integer value.
     * @return boolean true: each character is integer false: other
     */
    public static boolean isInteger(String str) {
        if (isBlank(str))
            return false;
        try {
            Integer.parseInt(str.trim());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * parses the specified string as a signed decimal long value
     *
     * @param str
     *            input string representation of an integer value.
     * @return boolean true: each character is long integer false: other
     */
    public static boolean isLong(String str) {
        if (isBlank(str))
            return false;
        try {
            Long.parseLong(str.trim());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * parses the specified string as a signed boolean value
     *
     * @param str
     *            input string
     * @return boolean true: str = true / TRUE (Ignore upper case or low case)
     *         false: other
     */
    public static boolean isBoolean(String str) {
        if (isBlank(str))
            return false;
        try {
            Boolean.parseBoolean(str.trim());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * checks the specified string as a double value
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        if (isBlank(str))
            return false;
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * checks the specified string as a Date value
     *
     * @param str
     *            the input string
     * @return boolean strΪʱ���ͷ���true�����򷵻�false
     */
    public static boolean isDate(String str) {
        if (isBlank(str))
            return false;
        try {
            java.sql.Date.valueOf(str.trim());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * checks the string arrays is all long values
     *
     * @param str
     *            the input string array
     * @return boolean strΪ���������鷵��true�����򷵻�false
     */
    public static boolean isLongs(String str[]) {
        for (int i = 0; i < str.length; i++) {
            if (!isLong(str[i]))
                return false;
        }
        return true;
    }

    /**
     * ����ַ�������str�Ƿ�Ϊ��������
     *
     * @param str
     *            Ҫ�����ַ���
     * @return boolean strΪ�������鷵��true�����򷵻�false
     */
    public static boolean isIntegers(String str[]) {
        for (int i = 0; i < str.length; i++)
            if (!isInteger(str[i]))
                return false;
        return true;
    }

    /**
     * ����ַ�������str�Ƿ�Ϊ����������
     *
     * @param str
     *            Ҫ�����ַ���
     * @return boolean strΪ���������鷵��true�����򷵻�false
     */
    public static boolean isBooleans(String str[]) {
        for (int i = 0; i < str.length; i++)
            if (!isBoolean(str[i]))
                return false;
        return true;
    }

    /**
     * ����ַ���str�Ƿ�Ϊʱ��
     *
     * @param str
     *            Ҫ�����ַ���
     * @return strΪʱ���ͷ���true�����򷵻�false
     */
    public static boolean isTimestamp(String str) {
        if (isBlank(str))
            return false;
        try {
            java.sql.Date.valueOf(str.trim());
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * ����ַ���str�Ƿ�Ϊ(yyyy-MM-dd HH:mm:ss)ģʽ��ʱ��
     * @param str Ҫ�����ַ���
     * @return strΪʱ���ͷ���true�����򷵻�false
     */
    public static boolean isFullTimestamp(String str) {
        if (isBlank(str))
            return false;
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(str.trim());
            return date != null;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * ���ַ�����ת��Ϊ����������
     *
     * @param str
     *            �ַ�����
     * @return Long[] ����������
     */
    public static Long[] stringsToLongs(String str[]) {
        Long lon[] = new Long[str.length];
        for (int i = 0; i < lon.length; i++)
            lon[i] = new Long(str[i]);
        return lon;
    }

    /**
     * ���ַ�����ת��Ϊ��������
     *
     * @param str
     *            �ַ�����
     * @return Integer[] ��������
     */
    public static Integer[] stringsToIntegers(String str[]) {
        Integer array[] = new Integer[str.length];
        for (int i = 0; i < array.length; i++)
            array[i] = new Integer(str[i]);
        return array;
    }

    /**
     * ���ַ�����ת��Ϊ����������
     *
     * @param str
     *            �ַ�����
     * @return Boolean[] ����������
     */
    public static Boolean[] stringsToBooleans(String str[]) {
        Boolean array[] = new Boolean[str.length];
        for (int i = 0; i < array.length; i++)
            array[i] = new Boolean(str[i]);
        return array;
    }

    /**
     * ���ַ�����ת��Ϊ����������
     *
     * @param str
     *            �ַ�����
     * @return double[] ����������
     */
    public static double[] stringsToDoubles(String str[]) {
        double array[] = new double[str.length];
        for (int i = 0; i < array.length; i++)
            array[i] = Double.parseDouble(str[i]);
        return array;
    }

    /**
     * �õ����ָ�ʽ������ַ���
     *
     * @param number
     *            Number����
     * @param minFractionDigits
     *            С����Сλ��
     * @param maxFractionDigits
     *            С�����λ��
     * @return String ��ʽ������ַ���
     */
    public static String formatNumber(Number number, int minFractionDigits,
                                      int maxFractionDigits) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMinimumFractionDigits(minFractionDigits);
        format.setMaximumFractionDigits(maxFractionDigits);
        return format.format(number);
    }

    /**
     * �ַ�������<br>
     * ����˸���ǰ׺�������׺��Ҫ������ʾ���ַ��������ڴ���ʱ�����⣬���ݱ��㷨�ɽ��JS������ʾʱ��ͬ������
     *
     * @param text
     *            ����
     * @param replaceStrs
     *            Ҫ������ʾ���ַ�������
     * @param beginStr
     *            ����ǰ׺����<font color=red>
     * @param endStr
     *            ������׺����</font>
     * @return
     */
    public static String heightLight(String text, String[] replaceStrs,
                                     String beginStr, String endStr) {
        if (text.length() == 0)
            return text;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < replaceStrs.length; i++) {
            String replaceStr = replaceStrs[i];
            int index = text.indexOf(replaceStr);
            if (index >= 0) {
                String afterStr = null;
                if (index > 0) {
                    String beforeStr = text.substring(0, index);
                    afterStr = text.substring(index + replaceStr.length());
                    str.append(heightLight(beforeStr, replaceStrs, beginStr,
                            endStr));
                } else
                    afterStr = text.substring(replaceStr.length());
                str.append(beginStr).append(replaceStr).append(endStr);
                str.append(heightLight(afterStr, replaceStrs, beginStr, endStr));
                break;
            }
        }
        if (str.length() == 0)
            return text;
        return str.toString();
    }

    /**
     * �滻ָ�����ַ�������Ϊһ���ַ���<br>
     * �ٶȱ�String.replaceAll��3�����ң���apache-common StringUtils.replace��2������
     *
     * @param text
     * @param replaceStrs
     * @param newStr
     * @return
     */
    public static String replaceAll(String text, String[] replaceStrs,
                                    String newStr) {
        if (text.length() == 0)
            return text;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < replaceStrs.length; i++) {
            String replaceStr = replaceStrs[i];
            int index = text.indexOf(replaceStr);
            if (index >= 0) {
                String afterStr = null;
                if (index > 0) {
                    String beforeStr = text.substring(0, index);
                    afterStr = text.substring(index + replaceStr.length());
                    str.append(replaceAll(beforeStr, replaceStrs, newStr));
                } else
                    afterStr = text.substring(replaceStr.length());
                str.append(newStr);
                str.append(replaceAll(afterStr, replaceStrs, newStr));
                break;
            }
        }
        if (str.length() == 0)
            return text;
        return str.toString();
    }

    /**
     * �滻ָ�����ַ���Ϊһ���ַ���<br>
     * �ٶȱ�String.replaceAll��3�����ң���apache-common StringUtils.replace��2������
     *
     * @param text
     * @param replaceStr
     * @param newStr
     * @return
     */
    public static String replaceAll(String text, String replaceStr,
                                    String newStr) {
        if (text.length() == 0)
            return text;
        StringBuilder str = new StringBuilder();
        int index = text.indexOf(replaceStr);
        if (index >= 0) {
            String afterStr = null;
            if (index > 0) {
                String beforeStr = text.substring(0, index);
                afterStr = text.substring(index + replaceStr.length());
                str.append(replaceAll(beforeStr, replaceStr, newStr));
            } else
                afterStr = text.substring(replaceStr.length());
            str.append(newStr);
            str.append(replaceAll(afterStr, replaceStr, newStr));
        }
        if (str.length() == 0)
            return text;
        return str.toString();
    }

    /**
     * �滻ָ�����ַ�������Ϊһ���ַ�������<br>
     * �ٶȱ�String.replaceAll��3�����ң���apache-common StringUtils.replace��2������
     *
     * @param text
     * @param replaceStrs
     * @param newStrs
     * @return
     */
    public static String replaceAllArray(String text, String[] replaceStrs,
                                         String[] newStrs) {
        if (text.length() == 0)
            return text;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < replaceStrs.length; i++) {
            String replaceStr = replaceStrs[i];
            int index = text.indexOf(replaceStr);
            if (index >= 0) {
                String afterStr = null;
                if (index > 0) {
                    String beforeStr = text.substring(0, index);
                    afterStr = text.substring(index + replaceStr.length());
                    str.append(replaceAllArray(beforeStr, replaceStrs, newStrs));
                } else
                    afterStr = text.substring(replaceStr.length());
                str.append(newStrs[i]);
                str.append(replaceAllArray(afterStr, replaceStrs, newStrs));
                break;
            }
        }
        if (str.length() == 0)
            return text;
        return str.toString();
    }

    /**
     * ����HTML(��&gt;,&lt;,&quot;,&amp;ת����>,<,",& )
     * @param html
     * @return
     */
    public static String decodeHTML(String html) {
        if (isBlank(html))
            return EMPTY;
        String[] replaceStr = { "&amp;", "&lt;", "&gt;", "&quot;" };
        String[] newStr = { "&", "<", ">", "\"" };
        return replaceAllArray(html, replaceStr, newStr);
    }

}
