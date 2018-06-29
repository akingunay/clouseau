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
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOExceptExpression

package class CommitmentExtractor {
	
	Resource resource
	NestedExpressionResolver resolver
	ExceptExpressionSimplifier simplifier
	
	package new(Resource resource, NestedExpressionResolver resolver, ExceptExpressionSimplifier simplifier) {
		this.resource = resource
		this.resolver = resolver
		this.simplifier = simplifier
	}
	
	package def List<Commitment> extract() {
		val commitments = new ArrayList
		resource.allContents.toIterable.filter(CLOCommitment).map[CLOCommitment c |
			new Commitment(
				c.name, c.debtor, c.creditor,
				simplifier.simplify(resolver.resolve(c.create)), 
				simplifier.simplify(resolver.resolve(c.detach)),
				simplifier.simplify(resolver.resolve(c.discharge))
				)
			].forEach[Commitment c | commitments.add(c)]
		commitments
	}
	
}