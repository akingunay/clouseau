package uk.ac.lancaster.scc.turtles.clouseau.generator;

/** This interface defines a binary expression. 
 * 
 * @author Akin Gunay
 *
 */
interface BinaryExpression extends Expression {

	/**
	 * Returns the left (first) element of the binary expression.
	 * 
	 * @return the expression that is the left element of the binary expression.
	 */
	Expression getRight();
	
	/**
	 * Returns the right (second) element of the binary expression.
	 * 
	 * @return the expression that is the right element of the binary expression.
	 */
	Expression getLeft();
}
