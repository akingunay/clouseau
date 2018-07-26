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
		if (left == null || right == null) {
			throw new NullPointerException();
		}
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
	public List<Configuration> getSatisfyingConfigurations() {
		List<Configuration> leftConfigurations = left.getSatisfyingConfigurations();
		List<Configuration> rightConfigurations = right.getSatisfyingConfigurations();
		List<Configuration> satisfyingConfigurations = new ArrayList<>();
		for (Configuration leftConfiguration : leftConfigurations) {
			for (Configuration rightConfiguration : rightConfigurations) {
				satisfyingConfigurations.add(leftConfiguration.extend(
						rightConfiguration.getNecessaryEvents(), 
						rightConfiguration.getExceptionEvents()));
			}
		}
		Set<String> allExceptionEventNamesOfRight = extractAllExceptionEventNames(rightConfigurations);
		for (Configuration leftConfiguration : leftConfigurations) {
			satisfyingConfigurations.add(leftConfiguration.extend(new ArrayList<>(0), allExceptionEventNamesOfRight));
			satisfyingConfigurations.add(leftConfiguration.extend(allExceptionEventNamesOfRight, new ArrayList<>(0)));
		}

		Set<String> allExceptionEventNamesOfLeft = extractAllExceptionEventNames(leftConfigurations);
		for (Configuration rightConfiguration : rightConfigurations) {
			satisfyingConfigurations.add(rightConfiguration.extend(new HashSet<>(), allExceptionEventNamesOfLeft));
			satisfyingConfigurations.add(rightConfiguration.extend(allExceptionEventNamesOfLeft, new HashSet<>()));
		}
		return satisfyingConfigurations;
	}
	
	private Set<String> extractAllExceptionEventNames(List<Configuration> eventConfigurations) {
		Set<String> exceptionEventNames = new HashSet<>();
		for (Configuration eventConfiguration : eventConfigurations) {
			exceptionEventNames.addAll(eventConfiguration.getExceptionEvents());
		}
		return exceptionEventNames;
	}
	
	@Override
	public List<String> getEventNames() {
		Set<String> includedEventNames = new HashSet<>();
		includedEventNames.addAll(left.getEventNames());
		includedEventNames.addAll(right.getEventNames());
		return new ArrayList<>(includedEventNames);
	}
	
	@Override
	public String toString() {
		return "(" + left + " or " + right + ")";
	}
}
