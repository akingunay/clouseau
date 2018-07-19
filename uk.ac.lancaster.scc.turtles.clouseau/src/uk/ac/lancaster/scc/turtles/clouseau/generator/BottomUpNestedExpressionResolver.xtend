package uk.ac.lancaster.scc.turtles.clouseau.generator

import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOExpression
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOCommitment
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOAndExpression
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOOrExpression
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLODischarged
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLODetached
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOCreated
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOBaseEvent
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOExceptExpression
import org.eclipse.emf.ecore.resource.Resource

package class BottomUpNestedExpressionResolver implements NestedExpressionResolver {
	
	Resource resource
	
	package new(Resource resource) {
		this.resource = resource
	}
	
	override Expression resolve(CLOExpression expression) {
		if (expression instanceof CLOBaseEvent) {
			new BaseEvent(expression.event.name)
		} else if (expression instanceof CLOCreated) {
			resolve(findRefferedExpression(expression.commitment.name, [CLOCommitment c | c.create]))
		} else if (expression instanceof CLODetached) {
			resolve(findRefferedExpression(expression.commitment.name, [CLOCommitment c | c.detach]))
		} else if (expression instanceof CLODischarged) {
			resolve(findRefferedExpression(expression.commitment.name, [CLOCommitment c | c.discharge]))
		} else if (expression instanceof CLOOrExpression) {
			new OrExpression(resolve(expression.left), resolve(expression.right))
		} else if (expression instanceof CLOAndExpression) {
			new AndExpression(resolve(expression.left), resolve(expression.right))
		} else if (expression instanceof CLOExceptExpression) {
			new ExceptExpression(resolve(expression.left), resolve(expression.right))
		} else {
			throw new MalformedExpressionException
		}
	}
	
	private def CLOExpression findRefferedExpression(String commitmentName, (CLOCommitment) => CLOExpression expressionOfCommitment) {
		expressionOfCommitment.apply(
			resource.
			allContents.
			toIterable.
			filter(CLOCommitment).
			findFirst[CLOCommitment commitment | commitment.name.equals(commitmentName)])
	}
}