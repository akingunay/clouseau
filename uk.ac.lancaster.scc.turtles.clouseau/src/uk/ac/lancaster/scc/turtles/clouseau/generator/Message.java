package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * @author Akin Gunay
 *
 */
class Message {

	private final String name;
	private final String sender;
	private final String receiver;
	private final Set<String> keyParameters;
	private final Set<String> inParameters;
	private final Set<String> outParameters;
	private final Set<String> nilParameters;
	private final Set<String> unknownParameters;

	/**
	 * 
	 * @param name
	 * @param sender
	 * @param receiver
	 */
	Message(final String name, final String sender, final String receiver) {
		if (name == null) {
			throw new NullPointerException();
		}
		this.name = name;
		this.sender = sender;
		this.receiver = receiver;
		this.keyParameters = new HashSet<>();
		this.inParameters = new HashSet<>();
		this.outParameters = new HashSet<>();
		this.nilParameters = new HashSet<>();
		this.unknownParameters = new HashSet<>();
	}

	Message(String name, String sender, String receiver, Set<String> keyParameters, Set<String> inParameters,
			Set<String> outParameters, Set<String> nilParameters, Set<String> unknownParameters) {
		this.name = name;
		this.sender = sender;
		this.receiver = receiver;
		this.keyParameters = new HashSet<>(keyParameters);
		this.inParameters = new HashSet<>(inParameters);
		this.outParameters = new HashSet<>(outParameters);
		this.nilParameters = new HashSet<>(nilParameters);
		this.unknownParameters = new HashSet<>(unknownParameters);
	}
	
	Message(Event event, String sender, String receiver) {
		this.name = event.getName() + "M";
		this.sender = sender;
		this.receiver = receiver;
		this.keyParameters = new HashSet<>(event.getKeys());
		this.inParameters = new HashSet<>();
		this.outParameters = new HashSet<>();
		this.nilParameters = new HashSet<>();
		this.unknownParameters = new HashSet<>(event.getAttributes());
	}

	String getName() {
		return name;
	}

	String getSender() {
		return sender;
	}

	String getSenderOrDefault(String defaultSender) {
		return sender != null ? sender : defaultSender;
	}

	String getReceiver() {
		return receiver;
	}

	String getReceiverOrDefault(String defaultReceiver) {
		return sender != null ? sender : defaultReceiver;
	}

	Set<String> getKeyParameters() {
		return Collections.unmodifiableSet(keyParameters);
	}

	Set<String> getInParameters() {
		return Collections.unmodifiableSet(inParameters);
	}

	Set<String> getOutParameters() {
		return Collections.unmodifiableSet(outParameters);
	}

	Set<String> getNilParameters() {
		return Collections.unmodifiableSet(nilParameters);
	}

	Set<String> getUnknownParameters() {
		return Collections.unmodifiableSet(unknownParameters);
	}

	/**
	 * Returns the parameters that are adorned IN or OUT.
	 *
	 * @return
	 */
	Set<String> getBoundParameters() {
		Set<String> boundParameters = new HashSet<>();
		boundParameters.addAll(inParameters);
		boundParameters.addAll(outParameters);
		return boundParameters;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + (inParameters == null ? 0 : inParameters.hashCode());
		result = prime * result + (keyParameters == null ? 0 : keyParameters.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (nilParameters == null ? 0 : nilParameters.hashCode());
		result = prime * result + (outParameters == null ? 0 : outParameters.hashCode());
		result = prime * result + (receiver == null ? 0 : receiver.hashCode());
		result = prime * result + (sender == null ? 0 : sender.hashCode());
		result = prime * result + (unknownParameters == null ? 0 : unknownParameters.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.sender, other.sender)) {
			return false;
		}
		if (!Objects.equals(this.receiver, other.receiver)) {
			return false;
		}
		if (!Objects.equals(this.keyParameters, other.keyParameters)) {
			return false;
		}
		if (!Objects.equals(this.inParameters, other.inParameters)) {
			return false;
		}
		if (!Objects.equals(this.outParameters, other.outParameters)) {
			return false;
		}
		if (!Objects.equals(this.nilParameters, other.nilParameters)) {
			return false;
		}
		if (!Objects.equals(this.unknownParameters, other.unknownParameters)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns a BSPL compliant String representation of this object.
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(sender).append(" -> ").append(receiver).append(" : ").append(name).append("[");
		for (String parameter : keyParameters) {
			if (inParameters.contains(parameter)) {
				str.append("in ").append(parameter).append(", ");
			}
		}
		for (String parameter : keyParameters) {
			if (outParameters.contains(parameter)) {
				str.append("out ").append(parameter).append(", ");
			}
		}
		for (String parameter : inParameters) {
			if (!keyParameters.contains(parameter)) {
				str.append("in ").append(parameter).append(", ");
			}
		}
		for (String parameter : nilParameters) {
			str.append("nil ").append(parameter).append(", ");
		}
		for (String parameter : outParameters) {
			if (!keyParameters.contains(parameter)) {
				str.append("out ").append(parameter).append(", ");
			}
		}
		for (String parameter : unknownParameters) {
			str.append("? ").append(parameter).append(", ");
		}
		return str.replace(str.length() - ", ".length(), str.length(), "]").toString();
	}

}
