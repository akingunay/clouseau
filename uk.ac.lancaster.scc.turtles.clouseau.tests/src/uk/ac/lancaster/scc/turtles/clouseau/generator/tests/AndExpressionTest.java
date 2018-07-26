package uk.ac.lancaster.scc.turtles.clouseau.generator.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import uk.ac.lancaster.scc.turtles.clouseau.generator.AndExpression;
import uk.ac.lancaster.scc.turtles.clouseau.generator.Configuration;
import uk.ac.lancaster.scc.turtles.clouseau.generator.EventExpression;

public class AndExpressionTest {

	@Test
	public void testGetSatisfyingConfigurations() {
		AndExpression expression = new AndExpression(new EventExpression("e1"), new EventExpression("e2"));
		List<Configuration> satisfyingConfigurations = expression.getSatisfyingConfigurations();
		assertEquals(1, satisfyingConfigurations.size());
		assertEquals(new Configuration(Arrays.asList(new String[] {"e1", "e2"}), new ArrayList<>(0)), satisfyingConfigurations.get(0));
	}
	
	@Test
	public void getGetEventNames( ) {
		AndExpression expression = new AndExpression(new EventExpression("e1"), new EventExpression("e2"));
		assertEquals(Arrays.asList(new String[] {"e1", "e2"}), expression.getEventNames());
	}

}
