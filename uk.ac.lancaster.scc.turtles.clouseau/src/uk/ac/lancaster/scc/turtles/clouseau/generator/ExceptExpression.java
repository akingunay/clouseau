package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.HashSet;
import java.util.Set;

class ExceptExpression implements BinaryExpression {

	private final Expression left;
	private final Expression right;
	
	ExceptExpression(final Expression left, final Expression right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Expression getLeft() {
		return left;
	}
	
	@Override
	public Expression getRight() {
		return right;
	}

	// This one is tricky to implement, since except is not commutative.
	// Also note that this assumes that this is a simplified except
	// expression. That is, if this expression is 'A except b', then
	// 'b' must be a base event (i.e., it is not compound expression).
	// Otherwise it throws IllegalStateException. Since this method is
	// supposed to be called only during generation and simplification
	// of except expression is done during parsing, this assumption must
	// hold.
	@Override
	public Set<EventConfiguration> getSatisfyingEventConfigurations() {
		if (!(right instanceof BaseEvent)) {
			throw new IllegalStateException("Computation of satisfying configurations requires all except expression to be fully simplified.");
		}
		Set<EventConfiguration> leftEventConfigurations = left.getSatisfyingEventConfigurations();
		Set<EventConfiguration> rightEventConfigurations = right.getSatisfyingEventConfigurations();
		Set<EventConfiguration> satisfyingEventConfigurations = new HashSet<>();
		for (EventConfiguration leftEventConfiguration : leftEventConfigurations) {
			for (EventConfiguration rightEventConfiguration : rightEventConfigurations) {
				// Since the right expression is assumed to be a base event, we always receive a
				// configuration of the form ({e}, {}) from the right. We should interpret this
				// configuration to capture the exception event.  Hence, we use 
				// exceptionEvents and necessaryEvents in reverse order when we extend
				// the configuration that we received from left.
				satisfyingEventConfigurations.add(leftEventConfiguration.extend(rightEventConfiguration.getExceptionEvents(), rightEventConfiguration.getNecessaryEvents()));
			}
		}
		return satisfyingEventConfigurations;
	}
	
	@Override
	public String toString() {
		return "(" + left + " except " + right + ")";
	}
	
}
