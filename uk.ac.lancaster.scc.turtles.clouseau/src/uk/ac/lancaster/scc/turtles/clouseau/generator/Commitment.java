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
public class Commitment {

	private final String name;
	private final String debtor;
	private final String creditor;
	private final Expression create;
	private final Expression detach;
	private final Expression discharge;
	
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
	public Commitment(final String name, final String debtor, final String creditor, final Expression create, final Expression detach, final Expression discharge) {
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
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public String getDebtor() {
		return debtor;
	}

	/**
	 * 
	 * @return
	 */
	public String getCreditor() {
		return creditor;
	}

	/**
	 * 
	 * @return
	 */
	public Set<String> getRoles() {
		return Collections.unmodifiableSet(roles);
	}
	
	/**
	 * 
	 * @return
	 */
	public Expression getCreate() {
		return create;
	}

	/**
	 * 
	 * @return
	 */
	public Expression getDetach() {
		return detach;
	}

	/**
	 * 
	 * @return
	 */
	public Expression getDischarge() {
		return discharge;
	}
	
	public List<String> getIncludedEventNames() {
		Set<String> includedEventNames = new HashSet<>();
		includedEventNames.addAll(create.getEventNames());
		includedEventNames.addAll(detach.getEventNames());
		includedEventNames.addAll(discharge.getEventNames());
		return new ArrayList<>(includedEventNames);
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


