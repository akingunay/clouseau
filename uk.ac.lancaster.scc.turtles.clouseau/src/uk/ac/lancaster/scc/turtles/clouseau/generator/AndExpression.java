package uk.ac.lancaster.scc.turtles.clouseau.generator;

class AndExpression implements Expression {
	
	private final Expression left;
	private final Expression right;
	
	AndExpression(final Expression left, final Expression right) {
		this.left = left;
		this.right = right;
	}
	
	Expression getLeft() {
		return left;
	}
	
	Expression getRight() {
		return right;
	}
}
