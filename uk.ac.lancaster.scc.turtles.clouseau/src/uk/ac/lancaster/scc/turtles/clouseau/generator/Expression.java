package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.List;

/**
 * This interface defines an immutable expression. Any class that implements
 * this interface must ensure that the operators and operands of the corresponding
 * expression cannot be modified.
 * 
 * @author Akin Gunay
 *
 */
interface Expression {

	/**
	 * Returns the list of configurations that satisfy this expression.
	 * 
	 * @return
	 */
	List<Configuration> getSatisfyingConfigurations();
	
	/**
	 * Returns the list of unique event names that are included by this expression.
	 * 
	 * @return
	 */
	List<String> getEventNames();
}

// TODO
// OrExp
// ExceptExp
// Configuration, better names for necessary and exception