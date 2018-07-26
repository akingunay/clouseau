package uk.ac.lancaster.scc.turtles.clouseau.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @author Akin Gunay
 *
 */
public class Event {

    private final String name;
    private final Set<String> attributes;
    private final Set<String> nillables;
    private final Set<String> keys;
    /**
     * 
     * @param name
     * @param attributes
     * @param keys
     */
    public Event(final String name, final Set<Attribute> attributes, final Set<String> keys) {
		this.name = name;
		this.attributes = new HashSet<String>(attributes.stream().
				map(a -> a.getName()).
				collect(Collectors.toSet()));
		this.nillables = new HashSet<String>(attributes.stream().
				filter(a -> a.isNillable()).
				map(a -> a.getName()).
				collect(Collectors.toSet()));
		this.keys = new HashSet<>(keys);
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
    public Set<String> getAttributes() {
		return Collections.unmodifiableSet(attributes);
	}

	/**
	 * 
	 * @return
	 */
    public Set<String> getKeys() {
		return Collections.unmodifiableSet(keys);
	}

	/**
	 * Returns a list of attributes, where keys appear first (in any order)
	 * and non-keys appear last (in any order). 
	 * 
	 * @return list of attributes, where keys appear first (in any order)
	 * and non-keys appear last (in any order)
	 */
    public List<String> getAttributesWithKeysFirst() {
		List<String> attributesWithKeysFrist = new ArrayList<>(keys);
		attributesWithKeysFrist.addAll(getNonkeyAttributes());
		return attributesWithKeysFrist;
	}
	
	/**
	 * Returns non-key attributes.
	 * 
	 * @return non-key attributes
	 */
    public Set<String> getNonkeyAttributes() {
		Set<String> nonkeyAttributes = new HashSet<>(attributes);
		nonkeyAttributes.removeAll(keys);
		return nonkeyAttributes;
	}
	
	/**
	 * 
	 * @param attribute
	 * @return
	 */
    public boolean isAttribute(final String attribute) {
        return attributes.contains(attribute);
    }
    
	/**
	 * 
	 * @param key
	 * @return
	 */
    public boolean isKey(final String key) {
        return keys.contains(key);
    }
    
    public boolean isNillable(final String attribute) {
    	return nillables.contains(attribute);
    }
    
    @Override
    public String toString() {
    	StringBuilder str = new StringBuilder("<Event>");
    	str.append("\n\tname : ").append(name).append("\n\tattributes : {");
    	for (String attribute : attributes) {
    		str.append(attribute).append(", ");
    	}
    	str.replace(str.length() - ", ".length(), str.length(), "}").append("\n\tkeys : {");
    	for (String key : keys) {
    		str.append(key).append(", ");
    	}
    	return str.replace(str.length() - ", ".length(), str.length(), "}").toString();
    }
}


