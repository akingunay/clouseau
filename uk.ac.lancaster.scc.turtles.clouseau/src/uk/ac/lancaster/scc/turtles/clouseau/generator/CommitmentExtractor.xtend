package uk.ac.lancaster.scc.turtles.clouseau.generator

import org.eclipse.emf.ecore.resource.Resource
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOCommitment
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOExpression
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOBaseEvent
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOCreated
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLODetached
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLODischarged
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOOrExpression
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOAndExpression
import java.util.ArrayList
import java.util.List

package class CommitmentExtractor {
	
	Resource resource
	
	package new(Resource resource) {
		this.resource = resource
	}
	
	package def List<Commitment> extract() {
		val commitments = new ArrayList
		resource.allContents.toIterable.filter(CLOCommitment).map[CLOCommitment c |
			new Commitment(
				c.name,
				c.debtor,
				c.creditor,
				new DNF(resolveNesting(c.create)), 
				new DNF(resolveNesting(c.detach)),
				new DNF(resolveNesting(c.discharge))
				)
			].forEach[Commitment c | commitments.add(c)]
		commitments
	}
	
	package def Expression resolveNesting(CLOExpression expression) {
		if (expression instanceof CLOBaseEvent) {
			new BaseEvent((expression as CLOBaseEvent).event.name)
		} else if (expression instanceof CLOCreated) {
			resolveNesting(findRefferedExpression((expression as CLOCreated).commitment.name, [CLOCommitment c | c.create]))
		} else if (expression instanceof CLODetached) {
			val create = resolveNesting(findRefferedExpression((expression as CLODetached).commitment.name, [CLOCommitment c | c.create]))
			val detach  = resolveNesting(findRefferedExpression((expression as CLODetached).commitment.name, [CLOCommitment c | c.detach]))
			new AndExpression(create, detach)
		} else if (expression instanceof CLODischarged) {
			val create = resolveNesting(findRefferedExpression((expression as CLODischarged).commitment.name, [CLOCommitment c | c.create]))
			val detach  = resolveNesting(findRefferedExpression((expression as CLODischarged).commitment.name, [CLOCommitment c | c.detach]))
			val discharge = resolveNesting(findRefferedExpression((expression as CLODischarged).commitment.name, [CLOCommitment c | c.discharge]))
			new OrExpression(new AndExpression(create, discharge), new AndExpression(detach, discharge))
		} else if (expression instanceof CLOOrExpression) {
			val orExpression = (expression as CLOOrExpression) 
			new OrExpression(resolveNesting(orExpression.left), resolveNesting(orExpression.right))
		} else if (expression instanceof CLOAndExpression) {
			val andExpression = (expression as CLOAndExpression)
			new AndExpression(resolveNesting(andExpression.left), resolveNesting(andExpression.right))
		} else {
			new BaseEvent("")
		}
	}
	
	package def CLOExpression findRefferedExpression(String commitmentName, (CLOCommitment) => CLOExpression expressionOfCommitment) {
		expressionOfCommitment.apply(
			resource.
			allContents.
			toIterable.
			filter(CLOCommitment).
			findFirst[CLOCommitment commitment | commitment.name.equals(commitmentName)])
	}
}