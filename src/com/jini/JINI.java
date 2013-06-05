package com.jini;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A well formatted Java INI Parser.
 * 
 * @author Sri Harsha Chilakapati
 */
public class JINI
{

    // The collection of sections
    private HashMap<String, Section> sections = null;

    /**
     * Create a empty JINI object. Useful for creating new INI files.
     */
    public JINI()
    {
        sections = new HashMap<String, Section>();
    }

    /**
     * Creates a JINI object with data parsed from a file even if it is present in the JAR file.
     * The file can't be saved if it is inside the JAR file.
     * 
     * @param name The name of the file.
     */
    public JINI(String name) throws Exception
    {
        this();
        String[] lines = toLines(getClass().getClassLoader().getResourceAsStream(name));
        parse(lines);
    }

    // Parse a collection of lines
    private void parse(String[] lines) throws Exception
    {
        // The default section name. Used to store properties without a section.
        String secName = "NULL";
        Section sec = new Section(secName);
        // Start parsing each line
        for (int i = 0; i < lines.length; i++)
        {
            // Trim the line to replace leading and trailing spaces.
            String line = lines[i].trim();
            
            if (line.startsWith("[") && line.endsWith("]"))
            {
                // If the line starts and ends with braces, it's a section
                sections.put(secName, sec);
                secName = trim(line);
                sec = new Section(secName);
            }
            else if (line.trim().length() == 0)
            {
                // Empty line. Ignore it.
            }
            else if (line.contains("="))
            {
                // If the line contains an "=" equals sign, it's a property statement
                String[] props = line.split("=");
                try
                {
                    if (props.length > 1)
                        // The first is the property name, the next is the value
                        sec.setProperty(props[0].trim(), props[1].trim());
                }
                catch (Exception e)
                {
                    // Invalid statement. Throw an exception.
                    throw new Exception("Invalid INI statement: " + line.trim());
                }
            }
        }
        // Add the last section.
        sections.put(secName, sec);
    }

    // Another trim method to remove the brackets from the section name
    private String trim(String str)
    {
        String result = "";
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if ((c != '[' && c != ']'))
            {
                // Add every char which is not a bracket.
                result += c;
            }
        }
        return result;
    }

    // Converts an input stream to an array of String's
    private String[] toLines(InputStream ins)
    {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
        try
        {
            // Read each line and add it to the ArrayList.
            String line = reader.readLine();
            while (line != null)
            {
                lines.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Convert the ArrayList to an array of strings
        String[] _lines = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++)
        {
            _lines[i] = lines.get(i);
        }
        // Return the array
        return _lines;
    }

    /**
     * Get a Section object for the name specified
     * @param secName The name of the section
     * @return The Section object
     */
    public Section getSection(String secName)
    {
        return sections.get(secName);
    }

    /**
     * Adds a section to the section list. This will overwrite if a section
     * with the same name already exists in the sections list.
     * @param sec The section to be added.
     */
    public void addSection(Section sec)
    {
        sections.put(sec.name, sec);
    }
    
    /**
     * Renames a section in the INI file.
     * @param oldName The old name of the section.
     * @param newName The new name of the section.
     */
    public void renameSection(String oldName, String newName)
    {
        Section sec = getSection(oldName);
        sec.name = newName;
        sections.remove(oldName);
        addSection(sec);
    }

    /**
     * Read a property from a section in the INI file.
     * @param secName The name of the section.
     * @param propName The name of the property.
     * @return The value of the property.
     */
    public String getProperty(String secName, String propName)
    {
        return getSection(secName).getProperty(propName).value;
    }

    /**
     * Set a property in a section in the INI file. This creates the
     * property if not exists or overwrites it.
     * @param secName The name of the section.
     * @param propName The name of the property.
     * @param propValue The value of the property.
     */
    public void setProperty(String secName, String propName, String propValue)
    {
        try
        {
            getSection(secName).setProperty(propName, propValue);
        }
        catch (Exception e)
        {
            Section sec = new Section(secName);
            sec.setProperty(propName, propValue);
            addSection(sec);
        }
    }
    
    /**
     * Renames a property in a section.
     * @param section The section name.
     * @param oldName The old name of the property.
     * @param newName The new name of the property.
     */
    public void renameProperty(String section, String oldName, String newName)
    {
        getSection(section).renameProperty(oldName, newName);
    }

    /**
     * Populates the whole INI as if in a file.
     * @param sep The line separator to be used. 
     * @return The whole new source file.
     */
    public String populate(String sep)
    {
        String data = "";
        Iterator<Section> i = sections.values().iterator();
        while (i.hasNext())
        {
            Section sec = i.next();
            data += sec.populate(sep);
        }
        return data;
    }

}
