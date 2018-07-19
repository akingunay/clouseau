package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.List;
import java.util.Set;

interface Expression {

	Set<EventConfiguration> getSatisfyingEventConfigurations();
	List<String> getIncludedEventNames();
}
