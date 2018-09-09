package org.change.hippo.server.http;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class HttpConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {

	private int keepAlive;

	public HttpConnectionKeepAliveStrategy(int keepAlive) {
		this.keepAlive = keepAlive;
	}

	@Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		//获取响应中Keep-Alive值
		HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		while (it.hasNext()) {
			HeaderElement he = it.nextElement();
			String param = he.getName();
			String value = he.getValue();
			if (value != null && param.equalsIgnoreCase("timeout")) {
				try {
					return Long.parseLong(value) * 1000;
				} catch (NumberFormatException ignore) {
				}
			}
		}
		return this.keepAlive;
	}

}
