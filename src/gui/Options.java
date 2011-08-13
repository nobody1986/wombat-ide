package gui;

import util.FileAccess;
import gnu.mapping.Procedure2;
import gnu.math.IntNum;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Store options.
 */
public class Options {
    final static String OPTIONS_FILE = "options.cfg";
    final static String SYNTAX_FILE = "syntax.cfg";
    
    static Map<String, String> data = new HashMap<String, String>();;
    static Map<String, Color> colors = new HashMap<String, Color>();;
    static Map<String, Integer> keywords = new HashMap<String, Integer>();;

    /**
     * Load default options.
     */
    static {
        reload();
    }

    public static void reload() {
        data.clear();
        colors.clear();
        keywords.clear();

        kawa.standard.Scheme kawa = new kawa.standard.Scheme();
        
        kawa.defineFunction(new Procedure2("cfg") {
			@Override
			public Object apply2(Object key, Object val) throws Throwable {
				data.put(key.toString(), val.toString());
				return null;
			}
        });
        
        kawa.defineFunction(new Procedure2("color") {
        	@Override
			public Object apply2(Object key, Object val) throws Throwable {
        		colors.put(key.toString(), parseColor(val.toString()));
				return null;
			}
        });
        
        kawa.defineFunction(new Procedure2("keyword") {
        	@Override
			public Object apply2(Object key, Object val) throws Throwable {
        		if (val instanceof IntNum)
        			keywords.put(key.toString(), ((IntNum) val).ival);
        		else
        			ErrorFrame.log("Unknown number format for indendation: " + val);
				return null;
			}
        });
        
        try {
			kawa.eval(FileAccess.getFile(OPTIONS_FILE));
			ErrorFrame.log(OPTIONS_FILE + " loaded.");
		} catch (Throwable ex) {
			ErrorFrame.log(OPTIONS_FILE + " failed to load: " + ex.getMessage());
		}
        
        try {
			kawa.eval(FileAccess.getFile(SYNTAX_FILE));
			SchemeDocument.reload();
			ErrorFrame.log(SYNTAX_FILE + " loaded.");
		} catch (Throwable ex) {
			ErrorFrame.log(SYNTAX_FILE + " failed to load: " + ex.getMessage());
		}
    }

    /**
     * Access values. Null if it doesn't exist.
     *
     * @param key The key to look up.
     * @return The value or null.
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     * Access values with a default it doesn't exist.
     *
     * @param key The key to look up.
     * @param def The default value.
     * @return The value or default.
     */
    public static String get(String key, String def) {
        if (data.containsKey(key))
            return data.get(key);
        else
            return def;
    }
    
    /**
     * Figure out a color.
     */
    static Color parseColor(String key) {
        Color c = null;

        // Load by name.
        if (c == null) {
            try {
                Field field = Class.forName("java.awt.Color").getField(key.toUpperCase());
                c = (Color) field.get(null);
            } catch (Exception e) {

            }
        }

        // Load by hex value.
        if (c == null) {
            try {
                c = Color.decode(key);
            } catch (Exception e) {
            }
        }

        // No idea what the color is. Don't do anything.
        if (c == null)
        {
            ErrorFrame.log("Unknown color format: " + key);
            return null;
        }

        // Finally return the dang things.
        else
        	return c;
    }
}
