package com.topcheer.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * ����������
 * 
 * @author lhs
 * @email	5235852@qq.com
 */
public class JavaUtils {
	public static String classpath = JavaUtils.class.getResource("/").getFile();
	
	/**
	 * ����ƥ��
	 * 
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static String[] match(String s, String pattern) {
		Matcher m = Pattern.compile(pattern).matcher(s);

		while (m.find()) {
			int n = m.groupCount();
			String[] ss = new String[n + 1];
			for (int i = 0; i <= n; i++) {
				ss[i] = m.group(i);
			}
			return ss;
		}
		return null;
	}

	/**
	 * ����ƥ��
	 * 
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static List<String[]> matchAll(String s, String pattern) {
		Matcher m = Pattern.compile(pattern).matcher(s);
		List<String[]> result = new ArrayList<String[]>();

		while (m.find()) {
			int n = m.groupCount();
			String[] ss = new String[n + 1];
			for (int i = 0; i <= n; i++) {
				ss[i] = m.group(i);
			}
			result.add(ss);
		}
		return result;
	}

	/**
	 * ����ƥ�䣬ָ����ʼλ��
	 * 
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static String[] firstMatch(String s, String pattern, int startIndex) {
		Matcher m = Pattern.compile(pattern).matcher(s);

		if (m.find(startIndex)) {
			int n = m.groupCount();
			String[] ss = new String[n + 1];
			for (int i = 0; i <= n; i++) {
				ss[i] = m.group(i);
			}
			return ss;
		}
		return null;
	}

	/**
	 * ����ƥ��
	 * 
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static boolean isMatch(String s, String pattern) {
		return s.matches(pattern);
	}

	public static boolean isAllMatch(String s, String pattern) {
		Matcher m = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(s);
		while (m.find()) {
			return true;
		}
		return false;
	}


	/**
	 * ��String ������Documnet
	 * @param tempbodyStr
	 * @return
	 * @throws Exception
	 */
	public static Document getDocument(String tempbodyStr) throws Exception{
		String bodyStr = tempbodyStr.replace("��", " ").replaceAll("&nbsp;", " ");
		// ����html parser
		DOMParser parser = new DOMParser();
		// ������ҳ��Ĭ�ϱ���
		parser.setFeature("http://xml.org/sax/features/namespaces", false);
		parser.setProperty("http://cyberneko.org/html/properties/default-encoding","GBK");
		java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(
				bodyStr.getBytes("GBK"));
		InputStreamReader fileIn = new InputStreamReader(bis, "GBK");
		BufferedReader in = new BufferedReader(fileIn);
		parser.parse(new InputSource(in));
		Document doc = parser.getDocument();
		return doc;
	}
	
	public static String XmlToString(Node node) {
		try {
			//org.apache.xml.serializer.TreeWalker;
			Source source = new DOMSource(node);
			StringWriter stringWriter = new StringWriter();
			Result result = new StreamResult(stringWriter);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(source, result);
			return stringWriter.getBuffer().toString().replaceAll("<\\?.*\\?>", "");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ����Textֵ
	 * @param node
	 * @return
	 */
	public static String getTextContent(Node node){
		if(node == null) return null;
		String textContent = node.getTextContent();
		if(textContent == null) return textContent;
		return textContent.trim();
	}
	
	/**
	 * ��ȡ��������ֵ
	 * @param node
	 * @param attrName
	 * @return
	 */
	public  static String getNodeValue(Node node, String attrName){
		if(node == null || node.getAttributes()==null) return null;
		Node attrNode = node.getAttributes().getNamedItem(attrName);
		if(attrNode == null || attrNode.getNodeValue() == null) return null;
		return attrNode.getNodeValue().trim();
	}

	/**
	 * ��ȡ��������ֵ
	 * @param node
	 * @param attrName
	 * @return
	 */
	public  static Node getNodeAttr(Node node, String attrName){
		if(node == null || node.getAttributes()==null) return null;
		Node attrNode = node.getAttributes().getNamedItem(attrName);
		return attrNode;
	}
	/**
	 * ��ȡ����Tag (XML)
	 * @param node
	 * @return
	 */
	public static String getTagContent(Node node){
		return JavaUtils.XmlToString(node);
	}
	
	public static String toPostParam(Map<String,String> map) throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder();
		for(Iterator<Entry<String, String>> it = map.entrySet().iterator(); it.hasNext();){
			Entry<String, String> entry = it.next();
			String value = entry.getValue().trim();
			sb.append(entry.getKey().trim()+"="+value+"&");
		}
		return sb.substring(0, sb.length()-1);
	}
	
	/**
	 * ��ȡproperties�ļ���ֵ
	 * @param fileName
	 * @param propertiesName
	 * @return
	 */
	public static String getPropertiesVal(String fileName, String propertiesName){
		ResourceBundle rb = ResourceBundle.getBundle(fileName);
		return StringUtil.repNullToStr(rb.getString(propertiesName));
	}
	
	/**
	 * ��ȡtransConfig.properties�ļ���ֵ
	 * @param propertiesName
	 * @return
	 */
	public static String getPropertiesVal(String propertiesName){
		ResourceBundle rb = ResourceBundle.getBundle("transConfig");
		return StringUtil.repNullToStr(rb.getString(propertiesName));
	}
	
	/**
	 * BigDecimal �ӷ�
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double add(double v1, double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	
	/**
	 * ���ո�
	 * @param str
	 * @param length
	 * @return
	 */
	public static String addBlackByLen(String str, int length) {
		if (StringUtil.isNull(str)) {
			str = "";
		}
		int strLen = str.getBytes().length;
		if (strLen == length) {
			return str;
		} else if (strLen < length) {
			int temp = length - strLen;
			String tem = "";
			for (int i = 0; i < temp; i++) {
				tem = tem + " ";
			}
			return str + tem;
		} else {
			return str.substring(0, length);
		}

	}
	
	public static void main(String[] args) {
		System.out.println("aaa"+addBlackByLen(null,2)+"bbb");
	}
}