package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a binary 'and' expression. The expression is modelled
 * as the root of a binary tree where left and right children are
 * also expressions.  This is an effectively immutable class.
 * 
 * 
 * @author Akin Gunay
 *
 */
public class AndExpression implements BinaryExpression {
	
	private final Expression left;
	private final Expression right;
	
	AndExpression(final Expression left, final Expression right) {
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
		return satisfyingEventConfigurations;
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
		return "(" + left + " and " + right + ")";
	}
}
