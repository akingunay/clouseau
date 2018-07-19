package uk.ac.lancaster.scc.turtles.clouseau.generator

import uk.ac.lancaster.scc.turtles.clouseau.generator.ExceptExpressionSimplifier

class TopDownExceptExpressionSimplifer implements ExceptExpressionSimplifier {
	
	override Expression simplify(Expression expression) {
		if (expression instanceof BaseEvent) {
			expression
		} else if (expression instanceof OrExpression) {
			new OrExpression(simplify(expression.left), simplify(expression.right))
		} else if (expression instanceof AndExpression) {
			new AndExpression(simplify(expression.left), simplify(expression.right))
		} else if (expression instanceof ExceptExpression) {
			val left = expression.left
			val right = expression.right
			if (right instanceof OrExpression) {
				new AndExpression(
					simplify(new ExceptExpression(left, right.left)), 
					simplify(new ExceptExpression(left, right.right))
				)
			} else if (right instanceof AndExpression) {
				new OrExpression(
					simplify(new ExceptExpression(left, right.left)),
					simplify(new AndExpression(left, right.right))
				)
			} else if (right instanceof ExceptExpression) {
				new OrExpression(
					simplify(new ExceptExpression(left, right.left)),
					simplify(new AndExpression(left, right.right))
				)
			} else {
				// this is the case where right is a base (it couldn't be
				// anything else after previous checks)
				new ExceptExpression(
					simplify(left),
					simplify(right)
				)
			}
		} else {
			throw new MalformedExpressionException
		}
	}

}