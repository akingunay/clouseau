package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Akin Gunay
 *
 */
class Commitment {

	private final String name;
	private final String debtor;
	private final String creditor;
	private final DNF create;
	private final DNF detach;
	private final DNF discharge;
	
	private final Set<String> roles;
	
	/**
	 * 
	 * @param name
	 * @param debtor
	 * @param creditor
	 * @param create
	 * @param detach
	 * @param discharge
	 */
	Commitment(final String name, final String debtor, final String creditor, final DNF create, final DNF detach, final DNF discharge) {
		super();
		this.name = name;
		this.debtor = debtor;
		this.creditor = creditor;
		this.create = create;
		this.detach = detach;
		this.discharge = discharge;
		this.roles = new HashSet<>();
		this.roles.add(debtor);
		this.roles.add(creditor);
	}

	/**
	 * 
	 * @return
	 */
	String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	String getDebtor() {
		return debtor;
	}

	/**
	 * 
	 * @return
	 */
	String getCreditor() {
		return creditor;
	}

	/**
	 * 
	 * @return
	 */
	Set<String> getRoles() {
		return Collections.unmodifiableSet(roles);
	}
	
	/**
	 * 
	 * @return
	 */
	DNF getCreate() {
		return create;
	}

	/**
	 * 
	 * @return
	 */
	DNF getDetach() {
		return detach;
	}

	/**
	 * 
	 * @return
	 */
	DNF getDischarge() {
		return discharge;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("<Commitment>");
		return str.append("\n\tname : ").append(name).
			append("\n\tdebtor : ").append(debtor).
			append("\n\tcreditor : ").append(creditor).
			append("\n\tcreate : ").append(create).
			append("\n\tdetach : ").append(detach).
			append("\n\tdischarge ").append(discharge).
			toString();
	}
}


