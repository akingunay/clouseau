package uk.ac.lancaster.scc.turtles.clouseau.generator

import java.util.HashSet
import org.eclipse.emf.ecore.resource.Resource
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOEvent
import java.util.ArrayList
import java.util.List
import uk.ac.lancaster.scc.turtles.clouseau.clouseau.CLOAttribute

package class EventExtractor {
	
	Resource resource
	
	package new(Resource resource) {
		this.resource = resource
	}
	
	package def List<Event> extract() {
		val events = new ArrayList
		resource.allContents.toIterable.filter(CLOEvent).
			map[CLOEvent e | new Event(e.name, new HashSet(e.attributes.map(CLOAttribute a | new Attribute(a.name, a.nillable))), new HashSet(e.keys))].
			forEach[Event e | events.add(e)]
		events
	}
	
}