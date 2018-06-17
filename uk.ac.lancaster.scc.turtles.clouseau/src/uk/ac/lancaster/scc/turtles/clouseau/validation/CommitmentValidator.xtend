package uk.ac.lancaster.scc.turtles.clouseau.validation

import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.Check
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOCommitment
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.ClouseauPackage
import org.eclipse.xtext.validation.EValidatorRegistrar

class CommitmentValidator extends AbstractDeclarativeValidator {
	
	// This method must be here for this validator to work.
    override register(EValidatorRegistrar registrar) {}
	
	@Check
	def checkDuplicateEventAttributes(CLOCommitment commitment) {
		if (commitment.debtor.equals(commitment.creditor)) {
			error("The same role cannot be the commitment's debtor and creditor.", ClouseauPackage.eINSTANCE.CLOCommitment_Debtor)
		}
	}
}