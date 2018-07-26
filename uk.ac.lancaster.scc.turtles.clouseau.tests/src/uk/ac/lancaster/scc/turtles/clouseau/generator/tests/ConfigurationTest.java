package uk.ac.lancaster.scc.turtles.clouseau.generator.tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

import uk.ac.lancaster.scc.turtles.clouseau.generator.Configuration;

public class ConfigurationTest {

	/**
	 * Test a valid extension of a configuration.
	 */
	@Test
	public void testExtend() {
		Collection<String> baseNecessaryEvents = new HashSet<>();
		baseNecessaryEvents.add("e1");
		baseNecessaryEvents.add("e2");
		Collection<String> baseExceptionEvents = new HashSet<>();
		baseExceptionEvents.add("e5");
		baseExceptionEvents.add("e6");
		Configuration baseConfiguration = new Configuration(baseNecessaryEvents, baseExceptionEvents);
		
		Collection<String> extensionNecessaryEvents = new HashSet<>();
		extensionNecessaryEvents.add("e3");
		extensionNecessaryEvents.add("e4");
		Collection<String> extensionExceptionEvents = new HashSet<>();
		extensionExceptionEvents.add("e7");
		extensionExceptionEvents.add("e8");
		Configuration extendedConfiguration = baseConfiguration.extend(extensionNecessaryEvents, extensionExceptionEvents);
		
		Collection<String> expectedNecessaryEvents = new HashSet<>();
		expectedNecessaryEvents.add("e1");
		expectedNecessaryEvents.add("e2");
		expectedNecessaryEvents.add("e3");
		expectedNecessaryEvents.add("e4");
		Collection<String> expectedExceptionEvents = new HashSet<>();
		expectedExceptionEvents.add("e5");
		expectedExceptionEvents.add("e6");
		expectedExceptionEvents.add("e7");
		expectedExceptionEvents.add("e8");
		Configuration expectedConfiguration = new Configuration(expectedNecessaryEvents, expectedExceptionEvents);
		
		assertEquals(expectedConfiguration, extendedConfiguration);
	}
	
	/**
	 * Test an IllegalArgumentException of the extend method due to the overlap between
	 * the given necessary and exception events.
	 */
	@Test (expected = IllegalArgumentException.class) 
	public void testExtendExceptionWhenGivenNecessaryAndExceptionEventsOverlap() {
		Configuration baseConfiguration = new Configuration();
		
		Collection<String> extensionNecessaryEvents = new HashSet<>();
		extensionNecessaryEvents.add("e1");
		Collection<String> extensionExceptionEvents = new HashSet<>();
		extensionExceptionEvents.add("e1");

		baseConfiguration.extend(extensionNecessaryEvents, extensionExceptionEvents);
	}


	/**
	 * Test the IllegalArgumentException of the extend method due to the overlap between
	 * the given necessary and original exception events.
	 */
	@Test (expected = IllegalArgumentException.class) 
	public void testExtendExceptionWhenGivenNecessarOverlapsOriginalException() {
		Collection<String> baseExceptionEvents = new HashSet<>();
		baseExceptionEvents.add("e1");
		Configuration baseConfiguration = new Configuration(new HashSet<>(), baseExceptionEvents);
		
		Collection<String> extensionNecessaryEvents = new HashSet<>();
		extensionNecessaryEvents.add("e1");

		baseConfiguration.extend(extensionNecessaryEvents, new HashSet<>());
	}
	
	/**
	 * Test the IllegalArgumentException of the extend method due to the overlap between
	 * the given exception and original necessary events.
	 */
	@Test (expected = IllegalArgumentException.class) 
	public void testExtendExceptionWhenGivenExceptionOverlapsOriginalNecessary() {
		Collection<String> baseNecessaryEvents = new HashSet<>();
		baseNecessaryEvents.add("e1");
		Configuration baseConfiguration = new Configuration(baseNecessaryEvents, new HashSet<>());
		
		Collection<String> extensionExceptionEvents = new HashSet<>();
		extensionExceptionEvents.add("e1");

		baseConfiguration.extend(new HashSet<>(), extensionExceptionEvents);
	}
}
