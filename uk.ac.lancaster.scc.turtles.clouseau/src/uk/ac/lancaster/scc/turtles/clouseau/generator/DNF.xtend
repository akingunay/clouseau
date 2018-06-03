package uk.ac.lancaster.scc.turtles.clouseau.generator

import java.util.List
import java.util.Set
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.Event
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.Expression
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.EventLabel
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.OrExpression
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.AndExpression
import java.util.ArrayList
import java.util.HashSet

class DNF {
	
	private List<Set<Event>> formula
	
	new(Expression expression) {
		formula = toDNF(expression)
	} 
	
	def List<Set<Event>> getFormula() {
		formula
	}
	
	def Set<Event> getEvents() {
		val events = new HashSet<Event>
		for (eventSet : formula) {
			events.addAll(eventSet)
		}
		events
	}
	
	private def List<Set<Event>> toDNF(Expression expression) {
		if (expression instanceof EventLabel) {
			base((expression as EventLabel).label)
    	} else if (expression instanceof OrExpression) {
    		val orExpression = expression as OrExpression
    		joinOr(toDNF(orExpression.left), toDNF(orExpression.right))
		} else if (expression instanceof AndExpression) {
			val andExpression = expression as AndExpression
    		joinAnd(toDNF(andExpression.left), toDNF(andExpression.right))
    	}
	}
	
	private def List<Set<Event>> base(Event event) {
		val Set<Event> conjunction = new HashSet
		conjunction.add(event)
		val List<Set<Event>> disjunction = new ArrayList
		disjunction.add(conjunction)
		disjunction
	}
	
	private def List<Set<Event>> joinOr(List<Set<Event>> left, List<Set<Event>> right) {
		val List<Set<Event>> disjunction = new ArrayList
		disjunction.addAll(left)
		disjunction.addAll(right)
		disjunction
	}
	
	private def List<Set<Event>> joinAnd(List<Set<Event>> left, List<Set<Event>> right) {
		val List<Set<Event>> disjunction = new ArrayList
		for (leftConjunction : left) {
			for (rightConjunction : right) {
				disjunction.add(mergeConjunctions(leftConjunction, rightConjunction))
			}
		}
		disjunction
	}

	private def Set<Event> mergeConjunctions(Set<Event> leftConjunction, Set<Event> rightConjunction) {
		val Set<Event> conjunction = new HashSet
		conjunction.addAll(leftConjunction)
		conjunction.addAll(rightConjunction)
		conjunction
	}

	override toString() {
		val str = new StringBuilder
		for (conjunction : formula) {
			str.append("(")
			for (event : conjunction) {
				str.append(event.name).append(" and ")
			}
			str.replace(str.length - " and ".length, str.length, ") or ")
				
		}
		str.replace(str.length - " or ".length, str.length, "").toString
	}
}