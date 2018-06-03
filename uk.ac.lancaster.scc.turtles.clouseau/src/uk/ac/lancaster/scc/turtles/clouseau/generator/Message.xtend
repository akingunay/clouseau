package uk.ac.lancaster.scc.turtles.clouseau.generator

import java.util.Set
import java.util.HashSet
import java.util.Objects

class Message {
	
	private String name
	private String sender
	private String receiver
	private Set<String> keyParameters
	private Set<String> inParameters
	private Set<String> outParameters
	private Set<String> nilParameters
	private Set<String> unknownParameters	// TODO this should be replaced with some validation
	
	new(String name, String sender, String receiver) {
		this.name = name
		this.sender = sender
		this.receiver = receiver
		keyParameters = new HashSet
		inParameters = new HashSet
		outParameters = new HashSet
		nilParameters = new HashSet
		unknownParameters = new HashSet
	}
	
	def addKeyParameter(String parameter) {
		keyParameters.add(parameter)
	}
	
	def addKeyParameters(Set<String> parameters) {
		keyParameters.addAll(parameters)
	}
	
	def addInParameter(String parameter) {
		inParameters.add(parameter)
	}
	
	def addInParameters(Set<String> parameters) {
		inParameters.addAll(parameters)
	}
	
	def addOutParameter(String parameter) {
		outParameters.add(parameter)
	}
	
	def addOutParameters(Set<String> parameters) {
		outParameters.addAll(parameters)
	}
	
	def addNilParameter(String parameter) {
		nilParameters.add(parameter)
	}
	
	def addUnknownParameter(String parameter) {
		unknownParameters.add(parameter)
	}
	
	def addUnknownParameters(Set<String> parameters) {
		unknownParameters.addAll(parameters)
	}
	
	def Set<String> getInParameters() {
		inParameters
	}
	
	def Set<String> getOutParameters() {
		outParameters
	}
	
	override int hashCode() {
		val prime = 31;
		var result = 1;
		result = prime * result + if (inParameters === null) 0 else  inParameters.hashCode
		result = prime * result + if (keyParameters === null) 0 else keyParameters.hashCode
		result = prime * result + if (name === null) 0 else name.hashCode
		result = prime * result + if (nilParameters === null) 0 else nilParameters.hashCode
		result = prime * result + if (outParameters === null) 0 else outParameters.hashCode
		result = prime * result + if (receiver === null) 0 else receiver.hashCode
		result = prime * result + if (sender === null) 0 else sender.hashCode
		result = prime * result + if (unknownParameters === null) 0 else unknownParameters.hashCode
		return result;
	}
	
	override boolean equals(Object obj) {
		if (this === obj)
			return true;
		if (obj === null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		val other = obj as Message;
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
	
	override String toString() {
		val str = new StringBuilder()
		str.append(sender).append(" -> ").append(receiver).append(" : ").append(name).append("[")
		for (parameter : keyParameters) {
			if (inParameters.contains(parameter)) {
				str.append("in ").append(parameter).append(", ")
			}
		}
		for (parameter : keyParameters) {
			if (outParameters.contains(parameter)) {
				str.append("out ").append(parameter).append(", ")
			}
		}
		for (parameter : inParameters) {
			if (!keyParameters.contains(parameter)) {
				str.append("in ").append(parameter).append(", ")
			}
		}
		for (parameter : nilParameters) {
			str.append("nil ").append(parameter).append(", ")
		}
		for (parameter : outParameters) {
			if (!keyParameters.contains(parameter)) {
				str.append("out ").append(parameter).append(", ")
			}
		}
		for (parameter : unknownParameters) {
			str.append("? ").append(parameter).append(", ")
		}
		str.replace(str.length - ", ".length, str.length, "]").toString
	}
}