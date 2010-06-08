package ro.cs.pub.pubsub;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.tools.rma.rma;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.PropertiesConfiguration;

import ro.cs.pub.pubsub.facilitator.agent.Facilitator;
import ro.cs.pub.pubsub.facilitator.agent.FacilitatorArguments;
import ro.cs.pub.pubsub.tera.agent.TeraAgent;
import ro.cs.pub.pubsub.tera.agent.TeraAgentArguments;

public class App {
	private static final String TERA_AGENT_PREFIX = "a_";
	private static final String FACILITATOR_AGENT = "facilitator";

	public static void main(String args[]) throws Exception {
		PropertiesConfiguration config = new PropertiesConfiguration(args[0]);
		App app = new App(config);
		app.start();
	}

	private PropertiesConfiguration configuration;

	public App(PropertiesConfiguration configuration)
			throws StaleProxyException {
		this.configuration = configuration;
	}

	public void start() throws StaleProxyException {
		// set up platform
		Runtime rt = Runtime.instance();
		rt.setCloseVM(true);
		Profile pMain = new ProfileImpl(null,
				configuration.getInt("jade.port"), null);
		pMain
				.setParameter("jade_core_messaging_MessageManager_poolsize",
						"100");
		pMain.setParameter("jade_core_messaging_MessageManager_maxqueuesize",
				"10000");

		// set up the main container
		AgentContainer mainContainer = rt.createMainContainer(pMain);
		if (configuration.getBoolean("jade.gui")) {
			AgentController rma = mainContainer.createNewAgent("rma", //
					rma.class.getCanonicalName(), new Object[0]);
			rma.start();
		}

		// timer dispatcher
		TimerDispatcherPool.buildInstance(50);

		// facilitator
		Object[] fArgs = { new FacilitatorArguments( //
				configuration.subset("pubsub.facilitator")) };
		mainContainer.createNewAgent(FACILITATOR_AGENT, //
				Facilitator.class.getCanonicalName(), fArgs).start();

		// TERA agents
		Set<AgentController> agents = new HashSet<AgentController>();

		AgentContainer container = null;
		int agentCount = configuration.getInt("pubsub.tera.agent.count");
		for (int id = 0; id < agentCount; id++) {
			if (id % 1000 == 0) {
				Profile p = new ProfileImpl(null, configuration
						.getInt("jade.port")
						+ id, null);
				container = rt.createAgentContainer(p);
			}

			Object[] args = { new TeraAgentArguments( //
					configuration.subset("pubsub.tera")) };
			agents.add(container.createNewAgent(TERA_AGENT_PREFIX + id,
					TeraAgent.class.getCanonicalName(), args));
		}

		System.out.println("Launching agents...");
		for (AgentController ac : agents) {
			ac.start();
		}
	}
}
