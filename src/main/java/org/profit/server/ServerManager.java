package org.profit.server;

/**
 * @author Kyia
 */
public interface ServerManager {

	public void startServers(int httpPort) throws Exception;

	public void startServers() throws Exception;

	public void stopServers() throws Exception;
}
