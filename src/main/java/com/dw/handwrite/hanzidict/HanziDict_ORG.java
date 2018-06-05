/*
 * Copyright (C) 2005 Jordan Kiang
 * jordan-at-com.dw.handwrite.kiang.org
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.dw.handwrite.hanzidict;

import com.dw.handwrite.hanzidict.CEDICTCharacterDictionary.CEDICTStreamProvider;
import com.dw.handwrite.hanzidict.CharacterDictionary.Entry.Definition;
import com.dw.handwrite.hanzilookup.HanziLookup;
import com.dw.handwrite.hanzilookup.HanziLookup.CharacterSelectionEvent;
import com.dw.handwrite.kiang.chinese.font.ChineseFontFinder;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.prefs.Preferences;

import static com.dw.handwrite.hanzilookup.HanziLookup.MATCH_COUNT_PREF_KEY;

/**
 * A messy adaptation of a previous dictionary app to work with
 * a newer version of the lookup engine.
 * 
 * Avert your eyes and don't look at this code.
 */
public class HanziDict_ORG extends JWindow implements HanziLookup.CharacterSelectionListener {

	static private final String RESOURCE_DICTIONARY_PREF_KEY = "resource_dictionary_pref";
	static private final String FILE_DICTIONARY_PREF_KEY = "file_dictionary_pref";
	static private final String USING_RESOURCE_DICTIONARY_PREF_KEY = "using_resource_dictionary_pref";
	static private final String FONT_PREF_KEY = "font";
	static private final String SAVE_OPTIONS_PREF = "save_options";
	
	static private final String DEFAULT_RESOURCE_DICTIONARY_PATH = "/cedict_ts.u8";
	//static private final String DEFAULT_RESOURCE_DICTIONARY_PATH = "handedict.u8";
	static private final String STROKE_DATA	= "/strokes.dat";
	//static private final String STROKE_DATA	= "/strokes-extended.dat";
	
    private HanziLookup lookupPanel;
	private JEditorPane definitionTextPane;          // pane to render the definition text
	
	private CharacterDictionary dictionary;
	
	private String resourceDictionaryPath = DEFAULT_RESOURCE_DICTIONARY_PATH;
	private String fileDictionaryPath = "";
	private boolean usingResourceDictionary = true;
	
	private Font font;
	private Preferences prefs;
	

	/**
	 * 初始化
	 */
    //@Override
	public void init() {
    	if(null != this.prefs) {
    		// If there are defined preferences, then we load the dictionary
    		// options from there.
    		this.loadDictionaryOptionsFromPreferences(this.prefs);
    	}
    	
    	if(null == this.font) {
    		// If the preferences weren't defined, or didn't include a font,
    		// then we try to find a Font capable of displaying Chinese.
    		this.font = ChineseFontFinder.getChineseFont();
    	}
    	
        this.lookupPanel = this.buildLookupPanel(this.font);
        
        if(null != this.prefs) {
        	// Now prime the lookup panel with any preferences that may be populated.
        	this.lookupPanel.loadOptionsFromPreferences(this.prefs);
        }
       
        JComponent definitionPane = this.buildDefinitionPane(this.font);
        
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this.lookupPanel, BorderLayout.WEST);
        contentPane.add(definitionPane, BorderLayout.CENTER);
         
        try {
	        if(this.usingResourceDictionary) {
	        	this.loadResourceDictionary(this.resourceDictionaryPath, null);
	        } else {
	        	this.loadFileDictionary(this.fileDictionaryPath, null);
	        }
        } catch(IOException ioe) {
        	JOptionPane.showMessageDialog(this, ioe.getMessage());
        }
        
    }
    
    /**
     * 加载cedict_ts.u8字典
     * @param prefs
     */
    private void loadDictionaryOptionsFromPreferences(Preferences prefs) {
    	this.resourceDictionaryPath = prefs.get(RESOURCE_DICTIONARY_PREF_KEY, DEFAULT_RESOURCE_DICTIONARY_PATH);

    	InputStream testStream = HanziLookup.class.getResourceAsStream(this.resourceDictionaryPath);
    	if(null == testStream) {
    		// test if we can read the stream.
    		// if we can then ok, otherwise revert to the default.
    		this.resourceDictionaryPath = DEFAULT_RESOURCE_DICTIONARY_PATH;
    	} else {
    		try {
    			testStream.close();
    		} catch(IOException ioe) {
    		}
    	}
    	
    	
		this.fileDictionaryPath = prefs.get(FILE_DICTIONARY_PREF_KEY, "");
		this.usingResourceDictionary = prefs.getBoolean(USING_RESOURCE_DICTIONARY_PREF_KEY, true);

		// 加载字体
		String fontsPath = System.getProperty("user.dir")  + File.separator + "fonts" + File.separator + "NotoSansCJKtc-Light.ttf";
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(fontsPath));
			this.font = font.deriveFont(Font.PLAIN, 20);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Write all preferences including the dictionary prefs and the internal HanziLookup prefs
     * to the given Preferences.
     * @param prefs
     */
    private void writeOptionsToPreferences(Preferences prefs) {
    	prefs.put(RESOURCE_DICTIONARY_PREF_KEY, this.resourceDictionaryPath);
    	prefs.put(FILE_DICTIONARY_PREF_KEY, this.fileDictionaryPath);
    	prefs.putBoolean(USING_RESOURCE_DICTIONARY_PREF_KEY, this.usingResourceDictionary);
    	prefs.put(FONT_PREF_KEY, this.lookupPanel.getFont().getName());
		prefs.putInt(MATCH_COUNT_PREF_KEY, 50);
    	this.lookupPanel.writeOptionsToPreferences(prefs);
    }
    
    /**
     * Load dictionary data from the given resource path.
     * The given ChangeListener (if non-null) will have stateChanged fired with
     * every new character loaded with a source of the CharacterDictionary.
     * The listener can then check the dictionary's current size.
     * This is useful for progress bars when loading.
     * 
     * @param resourcePath
     * @param progressListener
     * @throws IOException
     */
    private void loadResourceDictionary(String resourcePath, ChangeListener progressListener) throws IOException {
    	final URL resourceURL = this.getClass().getResource(resourcePath);
		if(null == resourceURL) {
			throw new MissingResourceException("Can't find resource: " + resourcePath, this.getClass().getName(), resourcePath);
		} else {
			this.loadDictionary(new CEDICTStreamProvider() {
				public InputStream getCEDICTStream() throws IOException {
					return resourceURL.openStream();
				}
			}, progressListener);
			
			HanziDict_ORG.this.resourceDictionaryPath = resourcePath;
			HanziDict_ORG.this.usingResourceDictionary = true;
		}
    }
    
    /**
     * Load the dictionary data from the given file on disk.
     * @param filePath
     * @param progressListener
     * @throws IOException
     */
    private void loadFileDictionary(String filePath, ChangeListener progressListener) throws IOException {
    	final File file = new File(filePath);
		if(!file.canRead()) {
			throw new IOException("Can't read from the specified file: " + filePath);
		} else {
			this.loadDictionary(new CEDICTStreamProvider() {
				public InputStream getCEDICTStream() throws IOException {
					return new FileInputStream(file);
				}
			}, progressListener);
			
			HanziDict_ORG.this.fileDictionaryPath = filePath;
			HanziDict_ORG.this.usingResourceDictionary = false;
		}
    }
    
    private void loadDictionary(CEDICTStreamProvider streamProvider, ChangeListener progressListener) throws IOException {
    	this.dictionary = new CEDICTCharacterDictionary(streamProvider, progressListener);
    }
    
    /**
     * @param font
     * @return the HanziLookup panel that shows the left side of the app.
     */
    private HanziLookup buildLookupPanel(Font font) {
        try {
        	HanziLookup lookup = new HanziLookup(STROKE_DATA, font);

        	lookup.addCharacterReceiver(this);
    
        	return lookup;
        
        } catch(IOException ioe) {
        	ioe.printStackTrace();
        	JOptionPane.showMessageDialog(this, "Error reading in strokes data!", "Error", JOptionPane.ERROR_MESSAGE);
        	System.exit(1);
        }
        
        // unreachable
        return null;
    }
    
    /**
     * @param font
     * @return the definition pane that goes on the right side.
     */
    private JComponent buildDefinitionPane(Font font) {
		// textPane displays the character definitions
		this.definitionTextPane = new JEditorPane();
		this.definitionTextPane.setContentType("text/html; charset=UTF-8");
		this.definitionTextPane.setEditable(false);
		
		// wrap the textPane in a scroll pane so that it can scroll if necessary
		JScrollPane textPaneScrollPane = new JScrollPane(this.definitionTextPane);
		textPaneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textPaneScrollPane.setPreferredSize(new Dimension(300, 300));
		textPaneScrollPane.setMinimumSize(new Dimension(10, 10));
		textPaneScrollPane.setBorder(BorderFactory.createTitledBorder("Info"));
		
		return textPaneScrollPane;
    }
    

    /**
     * 显示选中的文字
     * @param selectedChar the character
     * @param dictEntry the definition data
     */
    private void loadDefinitionData(char selectedChar, CharacterDictionary.Entry dictEntry) {
		char tradChar = dictEntry.getTraditional();
		char simpChar = dictEntry.getSimplified();
		
		char primaryChar;
		char secondaryChar;
		
		if(selectedChar == tradChar) {
			primaryChar = tradChar;
			secondaryChar = simpChar;
		} else {
			primaryChar = simpChar;
			secondaryChar = tradChar;
		}
    	
		StringBuffer paneText = new StringBuffer();
		
		paneText.append("<html>\n");
		paneText.append("\t<head>\n");
		paneText.append("\t\t<style type=\"text/css\">\n");
		
		Font font = this.getFont();
		if(null != font) {
			paneText.append("\t\tbody {font-family: ").append(this.getFont().getFamily()).append("; font-size: ").append(this.getFont().getSize()).append("}\n");
		}
		
		paneText.append("\t\t.characters {font-size: 150%}\n");
		paneText.append("\t\t</style>\n");
    	paneText.append("\t</head>\n");
    	paneText.append("\t<body>\n");
		
		paneText.append("<h1 class=\"characters\">").append(primaryChar);
		if(secondaryChar != primaryChar) {
			paneText.append("(").append(secondaryChar).append(")");
		}
		paneText.append("</h1>\n");
		paneText.append("<br>\n\n");
		
		Definition[] defs = dictEntry.getDefinitions();
		
		// display the data in an html list
		paneText.append("<ol>\n");
		// cycle through pronunciations
		for(int i = 0; i < defs.length; i++) {
			String pinyinString = Pinyinifier.pinyinify(defs[i].getPinyin());
			
			/* canDisplayUpTo not reliable, apparently
			if(this.getFont().canDisplayUpTo(pinyinString) > -1) {
				// preferably show pinyin tones with accented chars,
				// but if the font can't do that, then revert to tone digits appended.
				pinyinString = defs[i].getPinyin();
			}
			*/
			
			paneText.append("<li><b>").append(pinyinString).append("</b><br>\n");
			
			String[] translations = defs[i].getTranslations();
			
			// cycle through the definitions for this pronunciation
			for(int j = 0; j < translations.length; j++) {
				paneText.append(translations[j]);
				if(j < translations.length - 1) {
					paneText.append("; ");
				}
			}
			paneText.append("\n");
		}
		
		paneText.append("</ol>\n");
		paneText.append("\t</body>\n");
		paneText.append("</html>");
	
		this.definitionTextPane.setText(paneText.toString());	
		
		// make sure scroll centered at the top
		this.definitionTextPane.setCaretPosition(0);
    }
   
    /**
     * @see Component#getFont()
     */
    public Font getFont() {
    	return this.font;
    }
    
    /**
     * @see Component#setFont(Font)
     */
    public void setFont(Font font) {
    	this.font = font;
    	
    	if(null != this.lookupPanel) {
    		this.lookupPanel.setFont(font);
    	}
    
    	super.setFont(font);
    }
	
    /**
     * Load the definition pane with a blank definition for when there is no data available.
     * @param character
     */
	private void loadEmptyDefinition(char character) {
		this.definitionTextPane.setText("<html>\n<body>\nNo definition found.\n</body>\n</html>");
	}

	/**
	 * 点击备选文字事件
	 * @param e the event
	 */
	public void characterSelected(CharacterSelectionEvent e) {
		char selectedChar = e.getSelectedCharacter();
	    CharacterDictionary.Entry entry = this.dictionary.lookup(selectedChar);
	    
	    if(null != entry) {
	        this.loadDefinitionData(selectedChar, entry);
	    } else {
	        this.loadEmptyDefinition(selectedChar);
	    }
	}

	/**
	 * Main method for running as a stand-alone program.
	 * Brings up a new Frame with the program running in it.
	 */
	static public void main(String[] args) {
		// generate a new frame to load the applet into it
		final HanziDict_ORG hanziDict = new HanziDict_ORG();
		
		// It's running as app, so we attempt to load
		// preferences from the Java prefs api.
		final Preferences prefs = retrievePreferences();
		if(null != prefs) {
			// If we successfully retrieved some prefs then we load the options state from there.
			hanziDict.prefs = prefs;
		}
		
		// init as if it were an applet.
		// by manually invoking init instead of using the constructor
		// we unify the cases of stand-alone program and applet.
		hanziDict.init();

		hanziDict.pack();
		hanziDict.setVisible(true);
	}
	
	/**
	 * Would prefer if this method didn't actually create a node if it didn't already exist.
	 * Would like instead to give them an option of whether or not to save the node.
	 * We'll settle for deleting the node created here after the fact if they choose not to save.
	 * 
	 * @return preferences loaded using this class
	 */
	static private Preferences retrievePreferences() {
		Preferences prefs = null;
		
		try {
			prefs = Preferences.userNodeForPackage(HanziDict_ORG.class);
		} catch(Exception e) {
			// if for any reason we can't load preferences, we just return
			// null and expect the app to use the defaults.
			
			System.err.println("Unable to read preferences, loading defaults.");
			e.printStackTrace();
		}
		
		return prefs;
	}
}
