package uk.ac.lancaster.scc.turtles.clouseau.generator.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import uk.ac.lancaster.scc.turtles.clouseau.generator.Configuration;
import uk.ac.lancaster.scc.turtles.clouseau.generator.EventExpression;

public class EventExpressionTest {

	@Test
	public void testGetSatisfyingConfigurations() {
		EventExpression e = new EventExpression("e1");
		List<Configuration> satisfyingConfigurations = e.getSatisfyingConfigurations();
		assertEquals(1, satisfyingConfigurations.size());
		assertEquals(new Configuration(Arrays.asList(new String[] {"e1"}), new ArrayList<>(0)), satisfyingConfigurations.get(0));
	}
	
	@Test
	public void getGetEventNames( ) {
		EventExpression e = new EventExpression("e1");
		List<String> eventNames = e.getEventNames();
		assertEquals(1, eventNames.size());
		assertEquals(Arrays.asList(new String[] {"e1"}), eventNames);
	}

}
