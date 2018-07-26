package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents an immutable binary 'and' expression, where the two operands
 * are arbitrary expressions.
 * 
 * 
 * @author Akin Gunay
 *
 */
public class AndExpression implements BinaryExpression {
	
	private final Expression left;
	private final Expression right;
	
	/**
	 * 
	 * @param left
	 * @param right
	 */
	public AndExpression(final Expression left, final Expression right) {
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
		List<Configuration> satisfyingConfigurations = new ArrayList<>();
		for (Configuration leftConfiguration : left.getSatisfyingConfigurations()) {
			for (Configuration rightConfiguration : right.getSatisfyingConfigurations()) {
				satisfyingConfigurations.add(leftConfiguration.extend(
						rightConfiguration.getNecessaryEvents(), 
						rightConfiguration.getExceptionEvents()));
			}
		}
		return satisfyingConfigurations;
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
		return "(" + left + " and " + right + ")";
	}
}
