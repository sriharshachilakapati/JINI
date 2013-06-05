package com.jini;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents a section in the INI file.
 * 
 * @author Sri Harsha Chilakapati
 */
public class Section
{

    /** The name of the section. */
    public String name       = "";
    
    // The map of properties.
    private HashMap<String, Property> properties = null;

    /**
     * Creates a new section with a name.
     * @param secName The name of this section.
     */
    public Section(String secName)
    {
        name = secName;
        properties = new HashMap<String, Property>();
    }

    /**
     * Replaces an existing property with the information in the
     * Property object specified.
     * @param p The property to be added.
     */
    public void setProperty(Property p)
    {
        properties.put(p.name, p);
    }

    /**
     * Sets a property with a property name with it's value. It replaces
     * if the property already exists.
     * @param propName The name of the property.
     * @param propValue The value of the property.
     */
    public void setProperty(String propName, String propValue)
    {
        properties.put(propName, new Property(propName, propValue));
    }

    /**
     * Returns the property object of the property name.
     * @param propName The name of the property.
     * @return The Property object.
     */
    public Property getProperty(String propName)
    {
        return properties.get(propName);
    }
    
    /**
     * Renames a property.
     * @param oldName The old name of the property.
     * @param newName The new name og the property.
     */
    public void renameProperty(String oldName, String newName)
    {
        Property prop = getProperty(oldName);
        prop.name = newName;
        properties.remove(oldName);
        setProperty(prop);
    }

    /**
     * Creates a textual representation of this section as if this is present
     * in the INI file.
     * @param sep The line separator to be used.
     * @return The textual representation of this section.
     */
    public String populate(String sep)
    {
        if (properties.values().isEmpty())
        {
            return "" + sep;
        }
        String sec = "[" + name + ']' + sep;
        Iterator<Property> i = properties.values().iterator();
        while (i.hasNext())
        {
            Property prop = i.next();
            sec += prop.name + "=" + prop.value + sep;
        }
        return sec;
    }

}
