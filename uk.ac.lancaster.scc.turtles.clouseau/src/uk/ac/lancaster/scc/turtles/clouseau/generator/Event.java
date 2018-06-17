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
class Event {

    private final String name;
    private final Set<String> attributes;
    private final Set<String> keys;
    
    /**
     * 
     * @param name
     * @param parameters
     * @param keys
     */
    Event(final String name, final Set<String> parameters, final Set<String> keys) {
		this.name = name;
		this.attributes = new HashSet<>(parameters);
		this.keys = new HashSet<>(keys);
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
	Set<String> getAttributes() {
		return Collections.unmodifiableSet(attributes);
	}

	/**
	 * 
	 * @return
	 */
	Set<String> getKeys() {
		return Collections.unmodifiableSet(keys);
	}

	/**
	 * Returns a list of attributes, where keys appear first (in any order)
	 * and non-keys appear last (in any order). 
	 * 
	 * @return list of attributes, where keys appear first (in any order)
	 * and non-keys appear last (in any order)
	 */
	List<String> getAttributesWithKeysFirst() {
		List<String> attributesWithKeysFrist = new ArrayList<>(keys);
		attributesWithKeysFrist.addAll(getNonkeyAttributes());
		return attributesWithKeysFrist;
	}
	
	/**
	 * Returns non-key attributes.
	 * 
	 * @return non-key attributes
	 */
	Set<String> getNonkeyAttributes() {
		Set<String> nonkeyAttributes = new HashSet<>(attributes);
		nonkeyAttributes.removeAll(keys);
		return nonkeyAttributes;
	}
	
	/**
	 * 
	 * @param parameter
	 * @return
	 */
	boolean isAttribute(final String parameter) {
        return attributes.contains(parameter);
    }
    
	/**
	 * 
	 * @param key
	 * @return
	 */
    boolean isKey(final String key) {
        return keys.contains(key);
    }
    
    @Override
    public String toString() {
    	StringBuilder str = new StringBuilder("<Event>");
    	str.append("\n\tname : ").append(name).append("\n\tparameters : {");
    	for (String parameter : attributes) {
    		str.append(parameter).append(", ");
    	}
    	str.replace(str.length() - ", ".length(), str.length(), "}").append("\n\tkeys : {");
    	for (String key : keys) {
    		str.append(key).append(", ");
    	}
    	return str.replace(str.length() - ", ".length(), str.length(), "}").toString();
    }
}


