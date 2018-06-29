package uk.ac.lancaster.scc.turtles.clouseau.generator;

/**
 * Represents an attribute of an event. The attribute can
 * be set as nillable.  This is an immutable class.
 * 
 * @author Akin Gunay
 *
 */
class Attribute {

	private final String name;
	private final boolean nillable;
	
	Attribute(String name, boolean nillable) {
		this.name = name;
		this.nillable = nillable;
	}

	public String getName() {
		return name;
	}

	public boolean isNillable() {
		return nillable;
	}
	
}