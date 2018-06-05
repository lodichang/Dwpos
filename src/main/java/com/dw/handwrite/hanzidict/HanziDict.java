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
import com.dw.handwrite.hanzilookup.HanziLookup;
import com.dw.handwrite.hanzilookup.HanziLookup.CharacterSelectionEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.prefs.Preferences;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import com.dw.handwrite.kiang.chinese.font.ChineseFontFinder;
import javafx.embed.swing.JFXPanel;

/**
 * A messy adaptation of a previous dictionary app to work with
 * a newer version of the lookup engine.
 * 
 * Avert your eyes and don't look at this code.
 */
public abstract class HanziDict extends JPanel implements HanziLookup.CharacterSelectionListener {

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
	private JLabel definitionText;          // pane to render the definition text
	
	private CharacterDictionary dictionary;
	
	private String resourceDictionaryPath = DEFAULT_RESOURCE_DICTIONARY_PATH;
	private String fileDictionaryPath = "";
	private boolean usingResourceDictionary = true;
	
	private Font font;
	private Preferences prefs;

	public HanziDict() {
		//this.setUndecorated(true);
		showHandWrite();
		//this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		//setAlwaysOnTop(true);
	}



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
        
        JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createMatteBorder(10,10,10,10, new Color(52,73,94)));
		add(contentPane);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this.lookupPanel, BorderLayout.WEST);
        contentPane.add(definitionPane, BorderLayout.NORTH);

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
				@Override
				public InputStream getCEDICTStream() throws IOException {
					return resourceURL.openStream();
				}
			}, progressListener);
			
			HanziDict.this.resourceDictionaryPath = resourcePath;
			HanziDict.this.usingResourceDictionary = true;
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
				@Override
				public InputStream getCEDICTStream() throws IOException {
					return new FileInputStream(file);
				}
			}, progressListener);
			
			HanziDict.this.fileDictionaryPath = filePath;
			HanziDict.this.usingResourceDictionary = false;
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

    public abstract void close(String text);

    
    /**
     * @param font
     * @return the definition pane that goes on the right side.
     */
    private JComponent buildDefinitionPane(Font font) {
    	JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
    	panel.setPreferredSize(new Dimension(-1, 50));
    	JLabel title = new JLabel("當前：");
		title.setFont(font);
    	panel.add(title, BorderLayout.WEST);
		definitionText = new JLabel();
		definitionText.setFont(font.deriveFont(Font.BOLD, font.getSize()));
		definitionText.setPreferredSize(new Dimension((int)(panel.getPreferredSize().getWidth() * 0.6), 50));
		panel.add(definitionText, BorderLayout.CENTER);

		JButton deleteBtn = new JButton("刪除");
		deleteBtn.setFont(font);
		deleteBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (definitionText != null && !"".equals(definitionText.getText().trim())) {
					definitionText.setText(definitionText.getText().substring(0, definitionText.getText().length() - 1));
				}
			}
		});
		JButton clearBtn = new JButton("清空");
		clearBtn.setFont(font);
		clearBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				definitionText.setText("");
			}
		});
		JButton okBtn = new JButton("確定");
		okBtn.setFont(font);
		okBtn.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close(definitionText.getText());
				//HanziDict.this.dispose();
			}
		});
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridLayout(1, 3, 10, 10));
		btnPanel.add(deleteBtn);
		btnPanel.add(clearBtn);
		btnPanel.add(okBtn);
		panel.add(btnPanel, BorderLayout.EAST);

		return panel;
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

		this.definitionText.setText(this.definitionText.getText() + String.valueOf(primaryChar));
    }
   
    /**
     * @see Component#getFont()
     */
    @Override
	public Font getFont() {
    	return this.font;
    }
    
    /**
     * @see Component#setFont(Font)
     */
    @Override
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
		this.definitionText.setText("");
	}

	/**
	 * 点击备选文字事件
	 * @param e the event
	 */
	@Override
	public void characterSelected(CharacterSelectionEvent e) {
		char selectedChar = e.getSelectedCharacter();
	    CharacterDictionary.Entry entry = this.dictionary.lookup(selectedChar);
	    
	    if(null != entry) {
	        this.loadDefinitionData(selectedChar, entry);
	    } else {
	        this.loadEmptyDefinition(selectedChar);
	    }
	}


	public void showHandWrite() {
		// It's running as app, so we attempt to load
		// preferences from the Java prefs api.
		final Preferences prefs = retrievePreferences();
		if(null != prefs) {
			// If we successfully retrieved some prefs then we load the options state from there.
			this.prefs = prefs;
		}

		// init as if it were an applet.
		// by manually invoking init instead of using the constructor
		// we unify the cases of stand-alone program and applet.
		init();

		//pack();
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = HanziDict.this.getSize();
		if (frameSize.width > displaySize.width) {
			frameSize.width = displaySize.width;
		}

		//setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2);
		setVisible(true);
	}



	/**
	 * Main method for running as a stand-alone program.
	 * Brings up a new Frame with the program running in it.
	 */
	public static void main(String[] args) {
		new HanziDict() {
			@Override
			public void close(String text) {
				System.out.println(text);
			}
		};
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
			prefs = Preferences.userNodeForPackage(HanziDict.class);
		} catch(Exception e) {
			// if for any reason we can't load preferences, we just return
			// null and expect the app to use the defaults.
			
			System.err.println("Unable to read preferences, loading defaults.");
			e.printStackTrace();
		}
		
		return prefs;
	}
}
