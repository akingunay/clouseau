package uk.ac.lancaster.scc.turtles.clouseau.generator;

class OrExpression implements Expression {

	private final Expression left;
	private final Expression right;
	
	OrExpression(final Expression left, final Expression right) {
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
