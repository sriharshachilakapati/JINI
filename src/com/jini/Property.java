package com.jini;

/**
 * Represents a Property in the INI file.
 * 
 * @author Sri Harsha Chilakapati
 */
public class Property
{

    /** The name of the property **/
    public String name  = "";
    /** The value of the property **/
    public String value = "";

    /**
     * Constructs a new property.
     * @param propName The name of the property.
     * @param propValue The value of the property.
     */
    public Property(String propName, String propValue)
    {
        name = propName;
        value = propValue;
    }

}
