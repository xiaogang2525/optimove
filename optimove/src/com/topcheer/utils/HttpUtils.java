package com.topcheer.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 *
 * HTTP ???󹤾???
 * @author bincode
 * @email	5235852@qq.com
 */
public class HttpUtils {
	private static Logger logger = Logger.getLogger(HttpUtils.class);
	private static String tempfilepath = "/httptemp";
	private static Map<String, DefaultHttpClient> _threadHttpClient = new HashMap<String, DefaultHttpClient>();
	private static Map<String, HttpContext> _threadHttpContext = new HashMap<String, HttpContext>();

	static {
		tempfilepath = JavaUtils.classpath + tempfilepath;
		File tempfilepathFile = new File(tempfilepath);
		if (!tempfilepathFile.exists())
			tempfilepathFile.mkdirs();
	}

	/**
	 * ??ӡCookies??Ϣ
	 * @param httpContext
	 */
	public static void printCookies(HttpContext httpContext){
//		CookieStore cookieStore = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);
//		List<Cookie> cookies = cookieStore.getCookies();     
//		if (cookies.isEmpty()) {     
//			System.out.println("None");     
//		  } else {     
//		for (int i = 0; i < cookies.size(); i++) {   
//			System.out.println("- " + cookies.get(i).toString());   
//			}     
//		} 
	}
	
	/**
	 * ??ȡ????URL????ҳ????
	 */
	public static HttpClient createHttpClient() {
		DefaultHttpClient httpclient = _threadHttpClient.get("httpclient");
		if (httpclient != null) {
			return httpclient;
		}
		httpclient = new DefaultHttpClient();
		_threadHttpClient.put("httpclient", httpclient);

		try{
			TrustManager easyTrustManager = new X509TrustManager() {
	            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
	                //To change body of implemented methods use File | Settings | File Templates.
	            }

	            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
	                //To change body of implemented methods use File | Settings | File Templates.
	            }

	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return new java.security.cert.X509Certificate[0];  //To change body of implemented methods use File | Settings | File Templates.
	            }
	        };

			SSLContext sslcontext = SSLContext.getInstance("TLS");
	        sslcontext.init(null, new TrustManager[]{easyTrustManager}, null);
	        SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
	        Scheme sch = new Scheme("https", sf,443);

	        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
		}catch(Exception e){
			e.printStackTrace();
		}
        
		httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(final HttpRequest request,
					final HttpContext context) throws HttpException,
					IOException {
				// if (!request.containsHeader("Accept-Encoding")) {
				// request.addHeader("Accept-Encoding", "gzip");
				// }
				if (!request.containsHeader("Accept")) {
					request.addHeader("Accept", "*/*");
				}
				if (request.containsHeader("User-Agent")) {
					request.removeHeaders("User-Agent");
				}
				if (request.containsHeader("Connection")) {
					request.removeHeaders("Connection");
				}
				request.addHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:8.0) Gecko/20100101 Firefox/8.0");
				request.addHeader("Connection", "keep-alive");
//				request.addHeader("Referer", "http://services.51mch.com");
				// request.addHeader("Connection", "close");
			}

		});
		httpclient.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(final HttpResponse response,
					final HttpContext context) throws HttpException,
					IOException {
//				HttpEntity entity = response.getEntity();
			}
		});
		//httpclient.setRedirectHandler(new DefaultRedirectHandler());
		return httpclient;
	}

	private static HttpContext getHttpContext(String urlHost, String cookies) {
		HttpContext httpContext = _threadHttpContext.get("httpContext");

		if (httpContext != null) {
			printCookies(httpContext);
			return httpContext;
		}
		httpContext = new BasicHttpContext();
		CookieStore cookieStore = createCookieStore(urlHost, cookies);
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		_threadHttpContext.put("httpContext", httpContext);
		printCookies(httpContext);
		return httpContext;
	}

	public static void shutdownHttpClient() {
		_threadHttpContext.remove("httpContext");
		DefaultHttpClient httpclient = _threadHttpClient.get("httpclient");
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
		}
		_threadHttpClient.remove("httpclient");
	}

	public static CookieStore createCookieStore(String urlHost, String cookieStr) {
		// Create a local instance of cookie store
		CookieStore cookieStore = new BasicCookieStore();
		if (cookieStr == null || "".equals(cookieStr))
			return cookieStore;
		String domain = urlHost.substring(urlHost.indexOf("//")+2);
		if (null != cookieStr && !cookieStr.trim().equals("")) {
			String[] cookies = cookieStr.split(";");
			// userCookieList = new AttributeList();
			for (int i = 0; i < cookies.length; i++) {
				int _i = cookies[i].indexOf("=");
				if (_i != -1) {
					String name = cookies[i].substring(0, _i);
					String value = cookies[i].substring(_i + 1);
					BasicClientCookie _cookie = new BasicClientCookie(name,
							value);
					_cookie.setDomain(domain);
					cookieStore.addCookie(_cookie);
				}
			}
		}
		return cookieStore;
	}

	public static List<NameValuePair> createNameValuePair(String params) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (null != params && !params.trim().equals("")) {
			String[] _params = params.split("&");
			// userCookieList = new AttributeList();
			for (int i = 0; i < _params.length; i++) {
				int _i = _params[i].indexOf("=");
				if (_i != -1) {
					String name = _params[i].substring(0, _i);
					String value = _params[i].substring(_i + 1);
					nvps.add(new BasicNameValuePair(name, value));
				}
			}
		}
		return nvps;
	}
	
	public static NameValuePair[] createNameValuePairs(String params) {
		NameValuePair[] nvp = null;
		if (null != params && !params.trim().equals("")) {
			String[] _params = params.split("&");
			nvp = new NameValuePair[_params.length];
			for (int i = 0; i < _params.length; i++) {
				int _i = _params[i].indexOf("=");
				if (_i != -1) {
					String name = _params[i].substring(0, _i);
					String value = _params[i].substring(_i + 1);
					nvp[i] = new BasicNameValuePair(name, value);
				}
			}
		}
		return nvp;
	}

	public static String doGetBody(String url, String cookieStr) {
		url = url.replaceAll("###(.*)", "");
		try {
			String urlEx = url.substring(0, url.lastIndexOf("/"));
			String urlHost = url;
			try {
				urlHost = url.substring(0, url.indexOf("/", 9));
			} catch (Exception e) {
			}

			HttpClient httpclient = createHttpClient();
			HttpContext localContext = getHttpContext(urlHost, cookieStr);
			String resultBody = null;
			int _count = 0;
			String loadUrl = url;
			HttpGet httpget = null;
			while (_count++ < 5) {
				try {
					logger.info("????(" + _count + "):" + url + "==>" + loadUrl);
					//localContext.removeAttribute(DefaultRedirectHandler.REDIRECT_LOCATIONS);
					httpget = new HttpGet(loadUrl);

					HttpResponse response = httpclient.execute(httpget,
							localContext);
					String locationUrl = checkLocation(response);
					if (locationUrl != null) {
						loadUrl = locationUrl;
						if (!loadUrl.startsWith("/")&&loadUrl.indexOf("://") == -1)
							loadUrl = urlEx + loadUrl;
						else if (loadUrl.indexOf("://") == -1) {
							loadUrl = urlHost + loadUrl;
						}
						continue;
					}
					if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
						continue;
					}
					HttpEntity entity = response.getEntity();
					// Consume response content
					if (entity != null) {
						resultBody = EntityUtils.toString(entity);
						entity.consumeContent();
						locationUrl = checkLocation(resultBody);
						if (resultBody == null) {
						} else {
							locationUrl = checkLocation(resultBody);
							if (locationUrl != null) {
								loadUrl = locationUrl;
								if (!loadUrl.startsWith("/"))
									loadUrl = urlEx + loadUrl;
								else if (loadUrl.indexOf("://") == -1) {
									loadUrl = urlHost + loadUrl;
								}
							} else
								break;
						}
					}
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (httpget != null)
						httpget.abort();
				}
			}
			return resultBody;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File doGetFile(String url, String cookieStr) {
		url = url.replaceAll("###(.*)", "");
		String urlHost = url;
		try {
			urlHost = url.substring(0, url.indexOf("/", 9));
		} catch (Exception e) {
		}
		
		HttpClient httpclient = createHttpClient();
		HttpContext localContext = getHttpContext(urlHost, cookieStr);
		HttpGet httpget = new HttpGet(url);

		HttpResponse response;
		try {
			response = httpclient.execute(httpget, localContext);
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			HttpEntity entity = response.getEntity();
			String filename = new Date().getTime() + ".temp";
			if (entity != null) {
				BufferedInputStream bis = new BufferedInputStream(entity
						.getContent());
				File file = new File(tempfilepath + "/" + filename);
				FileOutputStream fs = new FileOutputStream(file);

				byte[] buf = new byte[1024];
				int len = bis.read(buf);
				if(len == -1 || len == 0){
					file.delete();
					file = null;
					entity.consumeContent();
					return file;
				}
				while (len != -1) {
					fs.write(buf, 0, len);
					len = bis.read(buf);
				}
				fs.close();
					
				entity.consumeContent();
				return file;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doPostBody(String url, String params,String cookieStr, String encode, boolean redirt) {
		url = url.replaceAll("###(.*)", "");
		String urlEx = url.substring(0, url.lastIndexOf("/"));
		String urlHost = url;
		try {
			urlHost = url.substring(0, url.indexOf("/", 9));
		} catch (Exception e) {
		}

		HttpClient httpclient = createHttpClient();
		HttpContext localContext = getHttpContext(urlHost, cookieStr);
		int _count = 0;
		String loadUrl = null;
		HttpPost httpost = null;
		String resultBody = null;

		while (_count++ < 5) {
			try {
				logger.info("?ύ(" + _count + "):" + url);
				httpost = new HttpPost(url);
//				httpost.setHeader("Referer", url);

				if (encode == null) {
					httpost.setHeader("Content-Type","text/html;charset=gb2312");
					List<NameValuePair> nvps = createNameValuePair(params); 
					httpost.setEntity(new UrlEncodedFormEntity(nvps,"gb2312"));
				} else {
					List<NameValuePair> nvps = createNameValuePair(params);
					try {
						httpost.setEntity(new UrlEncodedFormEntity(nvps,
										encode));
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				}

				HttpResponse response = httpclient.execute(httpost,localContext);
//				HttpResponse response = httpclient.execute(httpost);

				String locationUrl = checkLocation(response);
				if (locationUrl != null) {
					loadUrl = locationUrl;
					if (!loadUrl.startsWith("/")
							&& loadUrl.indexOf("://") == -1)
						loadUrl = urlEx + loadUrl;
					else if (loadUrl.indexOf("://") == -1) {
						loadUrl = urlHost + loadUrl;
					}
					break;
				}
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					continue;
				}
				HttpEntity entity = response.getEntity();
				// Consume response content
				if (entity != null) {
					resultBody = EntityUtils.toString(entity);
					entity.consumeContent();
					locationUrl = checkLocation(resultBody);
					if (resultBody == null) {
					} else {
						locationUrl = checkLocation(resultBody);
						if (locationUrl != null) {
							loadUrl = locationUrl;
							if (!loadUrl.startsWith("/"))
								loadUrl = urlEx + loadUrl;
							else if (loadUrl.indexOf("://") == -1) {
								loadUrl = urlHost + loadUrl;
							}
						} else
							break;
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (httpost != null)
					httpost.abort();
			}
		}
		System.out.println("loadUrl="+loadUrl);
//		if (loadUrl != null && redirt)
//			resultBody = doGetBody(loadUrl, null);
		return resultBody;
	}

	public static String doPostBody(String url, byte[] content,
			Header[] headers, String cookieStr, String encode, boolean redirt) {
		if (encode == null)
			encode = HTTP.UTF_8;

		String urlEx = url.substring(0, url.lastIndexOf("/"));
		String urlHost = url;
		try {
			urlHost = url.substring(0, url.indexOf("/", 9));
		} catch (Exception e) {
		}
		HttpClient httpclient = createHttpClient();
		HttpContext localContext = getHttpContext(urlHost, cookieStr);
		int _count = 0;
		String loadUrl = null;
		HttpPost httpost = null;
		String resultBody = null;

		while (_count++ < 5) {
			try {
				logger.info("?ύ(" + _count + "):" + url);
				httpost = new HttpPost(url);

				ByteArrayEntity byteArrayEntity = new ByteArrayEntity(content);
				httpost.setEntity(byteArrayEntity);

				httpost.setHeaders(headers);

				HttpResponse response = httpclient.execute(httpost,
						localContext);

				String locationUrl = checkLocation(response);
				if (locationUrl != null) {
					loadUrl = locationUrl;
					if (!loadUrl.startsWith("/")
							&& loadUrl.indexOf("://") == -1)
						loadUrl = urlEx + loadUrl;
					else if (loadUrl.indexOf("://") == -1) {
						loadUrl = urlHost + loadUrl;
					}
					break;
				}
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					continue;
				}
				HttpEntity entity = response.getEntity();
				// Consume response content
				if (entity != null) {
					resultBody = EntityUtils.toString(entity);
					entity.consumeContent();
					locationUrl = checkLocation(resultBody);
					if (resultBody == null) {
					} else {
						locationUrl = checkLocation(resultBody);
						if (locationUrl != null) {
							loadUrl = locationUrl;
							if (!loadUrl.startsWith("/"))
								loadUrl = urlEx + loadUrl;
							else if (loadUrl.indexOf("://") == -1) {
								loadUrl = urlHost + loadUrl;
							}
						} else
							break;
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (httpost != null)
					httpost.abort();
			}
		}
		if (loadUrl != null && redirt)
			resultBody = doGetBody(loadUrl, null);
		return resultBody;
	}

	
	public static String doPostBody(String url, Map<String,String> stringBody, Map<String,File> fileBody,
			Header[] headers, String cookieStr, String encode, boolean redirt) {
		if (encode == null)
			encode = HTTP.UTF_8;

		String urlEx = url.substring(0, url.lastIndexOf("/"));
		String urlHost = url;
		try {
			urlHost = url.substring(0, url.indexOf("/", 9));
		} catch (Exception e) {
		}
		HttpClient httpclient = createHttpClient();
		HttpContext localContext = getHttpContext(urlHost, cookieStr);
		int _count = 0;
		String loadUrl = null;
		HttpPost httpost = null;
		String resultBody = null;

		while (_count++ < 5) {
			try {
				logger.info("?ύ(" + _count + "):" + url);
				httpost = new HttpPost(url);
				httpost.setHeaders(headers);

				MultipartEntity reqEntity = new MultipartEntity();  
				for(Iterator<Entry<String, File>>it = fileBody.entrySet().iterator(); it.hasNext();){
					Entry<String, File> fileEntry = it.next();
					FileBody file = new FileBody(fileEntry.getValue());
					reqEntity.addPart(fileEntry.getKey(), file);
				}
				
				for(Iterator<Entry<String, String>>it = stringBody.entrySet().iterator(); it.hasNext();){
					Entry<String, String> stringEntry = it.next();
					StringBody str = new StringBody(stringEntry.getValue());
					reqEntity.addPart(stringEntry.getKey(), str);
				}
				//????????   
				httpost.setEntity(reqEntity);   
				
				HttpResponse response = httpclient.execute(httpost,
						localContext);

				String locationUrl = checkLocation(response);
				if (locationUrl != null) {
					loadUrl = locationUrl;
					if (!loadUrl.startsWith("/")
							&& loadUrl.indexOf("://") == -1)
						loadUrl = urlEx + loadUrl;
					else if (loadUrl.indexOf("://") == -1) {
						loadUrl = urlHost + loadUrl;
					}
					break;
				}
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					continue;
				}
				HttpEntity entity = response.getEntity();
				// Consume response content
				if (entity != null) {
					resultBody = EntityUtils.toString(entity);
					entity.consumeContent();
					locationUrl = checkLocation(resultBody);
					if (resultBody == null) {
					} else {
						locationUrl = checkLocation(resultBody);
						if (locationUrl != null) {
							loadUrl = locationUrl;
							if (!loadUrl.startsWith("/"))
								loadUrl = urlEx + loadUrl;
							else if (loadUrl.indexOf("://") == -1) {
								loadUrl = urlHost + loadUrl;
							}
						} else
							break;
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (httpost != null)
					httpost.abort();
			}
		}
		if (loadUrl != null && redirt)
			resultBody = doGetBody(loadUrl, null);
		return resultBody;
	}
	
	/**
	 * ?????Ƿ?????????ת????3?ַ???<br>
	 * <ol>
	 * <li>ͷ????????location:??????content-location:???????ش???302</li>
	 * <li>???ݲ??ְ?????meta http-equiv=refresh content="2;URL=..."??</li>
	 * <li>js?ű?ˢ?£?????Ϊ??"(?s)<script.{0,50}?>\\s*((document)|(window)|(this))\\.location(\\.href)?\\s*="</li>
	 * </ol>
	 */
	private static String checkLocation(HttpResponse response) {
		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
			if (header.getName().equalsIgnoreCase("location")
					|| header.getName().equalsIgnoreCase("content-location")) {
				return header.getValue();
			}
		}
		return null;
	}

	/**
	 * ?????Ƿ?????????ת????3?ַ???<br>
	 * <ol>
	 * <li>???ݲ??ְ?????meta http-equiv=refresh content="2;URL=..."??</li>
	 * <li>js?ű?ˢ?£?????Ϊ??"(?s)<script.{0,50}?>\\s*((document)|(window)|(this))\\.location(\\.href)?\\s*="</li>
	 * </ol>
	 */
	private static String checkLocation(String httpBody) {
		String locationUrl = null;
		// 2.
		String bodyLocationStr = "";
		if (httpBody.length() > 5120) {
			bodyLocationStr = httpBody.substring(0, 5120);// ̫??????ȡ????????
		} else {
			bodyLocationStr = httpBody;
		}
		bodyLocationStr = bodyLocationStr.replaceAll("<!--(?s).*?-->", "")
				.replaceAll("['\"]", "");// ȥ??ע?ͺ????Ų???

		int metaLocation = -1;
		metaLocation = bodyLocationStr.toLowerCase().indexOf("http-equiv=refresh");
		if (metaLocation != -1) {
			String locationPart = bodyLocationStr.substring(metaLocation,
					bodyLocationStr.indexOf(">", metaLocation));
			metaLocation = locationPart.toLowerCase().indexOf("url");
			if (metaLocation != -1) {
				// ?ٶ?url=...???? > ֮ǰ?????Ĳ???
				locationUrl = locationPart.substring(metaLocation + 4,
						locationPart.length()).replaceAll("\\s+[^>]*", "");
				return locationUrl;
			}
		}
		// 3.
		Matcher locationMath = Pattern
				.compile(
						"(?s)<script.{0,50}?>\\s*((document)|(window)|(this))\\.location(\\.href)?\\s*=")
				.matcher(httpBody.toLowerCase());
		if (locationMath.find()) {
			String[] cs = httpBody.substring(locationMath.end()).trim().split(
					"[> ;<]");
			locationUrl = cs[0];
			cs = null;
			return locationUrl;
		}
		// û??ת??
		return null;
	}
}
