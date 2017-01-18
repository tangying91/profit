package org.profit.main;

import org.profit.ProfitServer;
import org.profit.config.spring.AppSpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Kyia
 */
public class CommandLine {

	protected CommandLine() {

	}

	public static void main(String args[]) {
		CommandLine cli = new CommandLine();
		try {
			// Get configuration
			ProfitServer server = cli.getConfiguration(args);
			if (server == null) {
				return;
			}

			// Start the server
			server.start();

			cli.addShutdownHook(server);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addShutdownHook(final ProfitServer server) {
		Runnable shutdownHook = new Runnable() {
			@Override
			public void run() {
				server.stop();
			}
		};

		// Add shutdown hook
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Thread(shutdownHook));
	}

	protected ProfitServer getConfiguration(String args[]) throws Exception {
		ProfitServer server = null;
		if (args.length == 0) {
			System.out.println("Using default configuration.");

			AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppSpringConfig.class);
			//ctx.registerShutdownHook();

			server = new ProfitServer();
		}
		return server;
	}
}
