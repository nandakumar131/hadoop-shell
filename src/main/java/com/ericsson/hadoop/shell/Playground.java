package com.ericsson.hadoop.shell;

import java.net.InetSocketAddress;

import org.apache.mina.proxy.ProxyConnector;
import org.apache.mina.proxy.handlers.ProxyRequest;
import org.apache.mina.proxy.handlers.socks.SocksProxyConstants;
import org.apache.mina.proxy.handlers.socks.SocksProxyRequest;
import org.apache.mina.proxy.session.ProxyIoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Playground {

	public static void main(String[] args) throws Exception {

		String host = "127.0.0.1";
		int port = 22;

		String proxyHost = "127.0.0.1";
		int proxyPort = 22;

		NioSocketConnector socketConnector = new NioSocketConnector(Runtime
				.getRuntime().availableProcessors() + 1);
		ProxyConnector connector = new ProxyConnector(socketConnector);

		ProxyRequest req = new SocksProxyRequest(
				SocksProxyConstants.SOCKS_VERSION_4,
				SocksProxyConstants.ESTABLISH_TCPIP_STREAM,
				new InetSocketAddress(host, port), "");

		ProxyIoSession proxyIoSession = new ProxyIoSession(
				new InetSocketAddress(proxyHost, proxyPort), req);

		connector.setProxyIoSession(proxyIoSession);

		connector.connect();

		// **************************************************************************************
		// old code
		// **************************************************************************************

		/*
		 * SshClient client = SshClient.setUpDefaultClient(); client.start();
		 * ClientSession session = client.connect("localhost", 22).await()
		 * .getSession(); SshdSocketAddress local = new
		 * SshdSocketAddress("127.0.0.1", 6667); SshdSocketAddress remote = new
		 * SshdSocketAddress("0.0.0.0", 22);
		 * 
		 * session.startLocalPortForwarding(local, remote);
		 * session.authPassword("enanvad", "Redhat@1234"); ClientChannel channel
		 * = session.createChannel( ClientChannel.CHANNEL_EXEC, "ls");
		 * channel.setOut(System.out); channel.setErr(System.err);
		 * channel.open(); Thread.sleep(999999l);
		 * channel.waitFor(ClientChannel.CLOSED, 0);
		 */
	}

}
