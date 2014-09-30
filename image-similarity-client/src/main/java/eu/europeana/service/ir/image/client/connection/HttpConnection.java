/*
 * HttpConnector.java - europeana4j
 * (C) 2011 Digibis S.L.
 */
package eu.europeana.service.ir.image.client.connection;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import eu.europeana.service.ir.image.client.exception.ApiConnectionException;

/**
 * The class encapsulating simple HTTP access.
 *
 * @author Sergiu Gordea
 */
public class HttpConnection {

    private static final int CONNECTION_RETRIES = 3;
    private static final int TIMEOUT_CONNECTION = 120000;
    private static final int STATUS_OK_START = 200;
    private static final int STATUS_OK_END = 299;
    private static final String ENCODING = "UTF-8";
    private HttpClient httpClient = null;

    public String getURLContent(String url, Map<String, String> requestHeaders) throws IOException {
        HttpClient client = this.getHttpClient(CONNECTION_RETRIES, TIMEOUT_CONNECTION);
        GetMethod get = new GetMethod(url);
        if(requestHeaders != null){
        	for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
        		get.setRequestHeader(header.getKey(), header.getValue());	
			}
        	
        }

        try {
            client.executeMethod(get);

            if (get.getStatusCode() >= STATUS_OK_START && get.getStatusCode() <= STATUS_OK_END) {
                byte[] byteResponse = get.getResponseBody();
                String res = new String(byteResponse, ENCODING);
                return res;
            } else {
                throw new ApiConnectionException("Http Error: " + get.getStatusCode() + "\n" + get.getResponseBodyAsString());
            }

        } finally {
            get.releaseConnection();
        }
    }

    public String getURLContent(String url, String jsonParamName, String jsonParamValue) throws IOException {
        HttpClient client = this.getHttpClient(CONNECTION_RETRIES, TIMEOUT_CONNECTION);
        PostMethod post = new PostMethod(url);
        post.setParameter(jsonParamName, jsonParamValue);

        try {
            client.executeMethod(post);

            if (post.getStatusCode() >= STATUS_OK_START && post.getStatusCode() <= STATUS_OK_END) {
                byte[] byteResponse = post.getResponseBody();
                String res = new String(byteResponse, ENCODING);
                return res;
            } else {
                return null;
            }

        } finally {
        	post.releaseConnection();
        }
    }

    
    
    
//    public boolean writeURLContent(String url, OutputStream out) throws IOException {
//        return writeURLContent(url, out, null);
//    }
//
//    public boolean writeURLContent(String url, OutputStream out, String requiredMime) throws IOException {
//        HttpClient client = this.getHttpClient(CONNECTION_RETRIES, TIMEOUT_CONNECTION);
//        GetMethod getMethod = new GetMethod(url);
//        try {
//            client.executeMethod(getMethod);
//
//            Header tipoMimeHead = getMethod.getResponseHeader("Content-Type");
//            String tipoMimeResp = "";
//            if (tipoMimeHead != null) {
//                tipoMimeResp = tipoMimeHead.getValue();
//            }
//
//            if (getMethod.getStatusCode() >= STATUS_OK_START && getMethod.getStatusCode() <= STATUS_OK_END
//                    && ((requiredMime == null) || ((tipoMimeResp != null) && tipoMimeResp.contains(requiredMime)))) {
//                InputStream in = getMethod.getResponseBodyAsStream();
//
//                // Copy input stream to output stream
//                byte[] b = new byte[4 * 1024];
//                int read;
//                while ((read = in.read(b)) != -1) {
//                    out.write(b, 0, read);
//                }
//
//                getMethod.releaseConnection();
//                return true;
//            } else {
//                return false;
//            }
//
//        } finally {
//            getMethod.releaseConnection();
//        }
//    }

   
    private HttpClient getHttpClient(int connectionRetry, int conectionTimeout) {
        if (this.httpClient == null) {
            HttpClient client = new HttpClient();
            
            //configure retry handler
            client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(connectionRetry, false));

            //when using a http proxy
            String proxyHost = System.getProperty("http.proxyHost");
            if ((proxyHost != null) && (proxyHost.length() > 0)) {
                String proxyPortSrt = System.getProperty("http.proxyPort");
                if (proxyPortSrt == null) {
                    proxyPortSrt = "8080";
                }
                int proxyPort = Integer.parseInt(proxyPortSrt);

                client.getHostConfiguration().setProxy(proxyHost, proxyPort);
            }

            //configure timeouts
            boolean bTimeout = false;
            String connectTimeOut = System.getProperty("sun.net.client.defaultConnectTimeout");
            if ((connectTimeOut != null) && (connectTimeOut.length() > 0)) {
                client.getParams().setIntParameter("sun.net.client.defaultConnectTimeout", Integer.parseInt(connectTimeOut));
                bTimeout = true;
            }
            String readTimeOut = System.getProperty("sun.net.client.defaultReadTimeout");
            if ((readTimeOut != null) && (readTimeOut.length() > 0)) {
                client.getParams().setIntParameter("sun.net.client.defaultReadTimeout", Integer.parseInt(readTimeOut));
                bTimeout = true;
            }
            if (!bTimeout) {
                client.getParams().setIntParameter(HttpMethodParams.SO_TIMEOUT, conectionTimeout);
            }

            this.httpClient = client;
        }
        return this.httpClient;
    }
}
