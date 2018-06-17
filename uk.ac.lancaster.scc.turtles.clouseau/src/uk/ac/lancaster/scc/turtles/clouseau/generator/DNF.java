package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Akin Gunay
 *
 */
class DNF {

	// The DNF is represented as a list of disjunctive sets,
	// where each set is a conjunction of event names.
	private final List<Conjunction> conjunctions;
	private final Set<String> eventNames;
	
	/**
	 * 
	 * @param expression
	 */
	DNF(final Expression expression) {
		conjunctions = toDNF(expression);
		eventNames = new HashSet<>();
		conjunctions.forEach(c -> eventNames.addAll(c.getEventNames()));
	} 
	
	/**
	 * Returns all the event names included by this DNF.
	 * 
	 * @return set of event names that are included by this DNF
	 */
	Set<String> getEventNames() {
		return Collections.unmodifiableSet(eventNames);
	}
	
	List<Conjunction> getConjunctions() {
		return Collections.unmodifiableList(conjunctions);
	}
	
	// Creates a DNF object from a given Expression.
	private List<Conjunction> toDNF(final Expression expression) {
		List<Conjunction> conjunctions = null;
		if (expression instanceof BaseEvent) {
			conjunctions = handleBaseEvent(((BaseEvent) expression).getEventName());
    	} else if (expression instanceof OrExpression) {
    		OrExpression orExpression = (OrExpression) expression;
    		conjunctions = mergeDisjunctionOfDNFs(toDNF(orExpression.getLeft()), toDNF(orExpression.getRight()));
		} else if (expression instanceof AndExpression) {
			AndExpression andExpression = (AndExpression) expression;
			conjunctions = mergeConjunctionOfDNFs(toDNF(andExpression.getLeft()), toDNF(andExpression.getRight()));
    	} else {
    		// TODO we should not reach here if parsing is done right
    	}
		return conjunctions;
	}

	// Creates a DNF formula from a single base event, which is just the name of the event.
	// [{eventName}] <- eventName
	private List<Conjunction> handleBaseEvent(final String eventName) {
		List<Conjunction> disjunction = new ArrayList<>();
		Set<String> conjunction = new HashSet<>();
		conjunction.add(eventName);
		disjunction.add(new Conjunction(conjunction));
		return disjunction;
	}
	
	// Merges two DNF formula that are composed via a disjunction, into a single DNF formula.
	// DNF <- DNF1 OR DNF2
	private List<Conjunction> mergeDisjunctionOfDNFs(final List<Conjunction> leftConjunctions, final List<Conjunction> rightConjunctions) {
		List<Conjunction> conjunctions = new ArrayList<>();
		conjunctions.addAll(leftConjunctions);
		conjunctions.addAll(rightConjunctions);
		return conjunctions;
	}

	// Merges two DNF formula that are composed via a conjunction, into a single DNF formula.
	// DNF <- DNF1 AND DNF2
	private List<Conjunction> mergeConjunctionOfDNFs(final List<Conjunction> leftConjunctions, final List<Conjunction> rightConjunctions) {
		List<Conjunction> conjunctions = new ArrayList<>();
		for (Conjunction leftConjunction : leftConjunctions) {
			for (Conjunction rightConjunction : rightConjunctions) {
				conjunctions.add(mergeConjunctionOfEventNames(leftConjunction, rightConjunction));
			}
		}
		return conjunctions;
	}

	// Merges two conjunctions of event names into a single conjunction of event names.
	// {a1, ..., aN, b1, ..., bN} <- {a1, ..., aN} AND {b1, ..., bM}
	private Conjunction mergeConjunctionOfEventNames(final Conjunction leftConjunction, final Conjunction rightConjunction) {
		Set<String> conjunction = new HashSet<>();
		conjunction.addAll(leftConjunction.getEventNames());
		conjunction.addAll(rightConjunction.getEventNames());
		return new Conjunction(conjunction);
	}

	@Override 
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Conjunction conjunction : conjunctions) {
			str.append("(");
			for (String eventName : conjunction.getEventNames()) {
				str.append(eventName).append(" and ");
			}
			str.replace(str.length() - " and ".length(), str.length(), ") or ");
		}
		return str.replace(str.length() - " or ".length(), str.length(), "").toString();
	}
	
}
