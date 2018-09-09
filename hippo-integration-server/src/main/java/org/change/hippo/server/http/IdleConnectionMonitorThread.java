package org.change.hippo.server.http;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionMonitorThread extends Thread {
	private final HttpClientConnectionManager connMgr;
	private int idleTime;
	private boolean shutdown = false;

	public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr, int idleTime) {
        super.setDaemon(true);
		this.connMgr = connMgr;
		this.idleTime = idleTime;
	}

	public void shutdown() {
		shutdown = true;
	}

	@Override
    public void run() {
		while (!shutdown) {
			try {
				synchronized (connMgr) {
					//每2秒清理一次连接池
					Thread.sleep(2000);
					//关闭过期连接
					connMgr.closeExpiredConnections();
//					//关闭空闲的连接
//					connMgr.closeIdleConnections(idleTime, TimeUnit.SECONDS);
				}
			} catch (InterruptedException e) {
			}
		}
	}
}
