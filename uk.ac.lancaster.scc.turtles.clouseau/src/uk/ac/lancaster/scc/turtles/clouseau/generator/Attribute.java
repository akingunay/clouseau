package uk.ac.lancaster.scc.turtles.clouseau.generator;

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
