package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a binary 'or' expression. The expression is modelled
 * as the root of a binary tree where left and right children are
 * also expressions.  This is an immutable effectively immutable class.
 * 
 * 
 * @author Akin Gunay
 *
 */
class OrExpression implements BinaryExpression {

	private final Expression left;
	private final Expression right;
	
	OrExpression(final Expression left, final Expression right) {
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
	
	@Override
	public Set<EventConfiguration> getSatisfyingEventConfigurations() {
		Set<EventConfiguration> leftEventConfigurations = left.getSatisfyingEventConfigurations();
		Set<EventConfiguration> rightEventConfigurations = right.getSatisfyingEventConfigurations();
		Set<EventConfiguration> satisfyingEventConfigurations = new HashSet<>();
		for (EventConfiguration leftEventConfiguration : leftEventConfigurations) {
			for (EventConfiguration rightEventConfiguration : rightEventConfigurations) {
				satisfyingEventConfigurations.add(leftEventConfiguration.extend(rightEventConfiguration.getNecessaryEvents(), rightEventConfiguration.getExceptionEvents()));
			}
		}
		Set<String> allExceptionEventNamesOfRight = extractAllExceptionEventNames(rightEventConfigurations);
		for (EventConfiguration leftEventConfiguration : leftEventConfigurations) {
			satisfyingEventConfigurations.add(leftEventConfiguration.extend(new HashSet<>(), allExceptionEventNamesOfRight));
			satisfyingEventConfigurations.add(leftEventConfiguration.extend(allExceptionEventNamesOfRight, new HashSet<>()));
		}

		Set<String> allExceptionEventNamesOfLeft = extractAllExceptionEventNames(leftEventConfigurations);
		for (EventConfiguration rightEventConfiguration : rightEventConfigurations) {
			satisfyingEventConfigurations.add(rightEventConfiguration.extend(new HashSet<>(), allExceptionEventNamesOfLeft));
			satisfyingEventConfigurations.add(rightEventConfiguration.extend(allExceptionEventNamesOfLeft, new HashSet<>()));
		}
		return satisfyingEventConfigurations;
	}
	
	private Set<String> extractAllExceptionEventNames(Set<EventConfiguration> eventConfigurations) {
		Set<String> exceptionEventNames = new HashSet<>();
		for (EventConfiguration eventConfiguration : eventConfigurations) {
			exceptionEventNames.addAll(eventConfiguration.getExceptionEvents());
		}
		return exceptionEventNames;
	}
	
	@Override
	public List<String> getIncludedEventNames() {
		Set<String> includedEventNames = new HashSet<>();
		includedEventNames.addAll(left.getIncludedEventNames());
		includedEventNames.addAll(right.getIncludedEventNames());
		return new ArrayList<>(includedEventNames);
	}
	
	@Override
	public String toString() {
		return "(" + left + " or " + right + ")";
	}
}
