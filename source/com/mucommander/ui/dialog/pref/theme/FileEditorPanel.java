/*
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2007 Maxence Bernard
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mucommander.ui.dialog.pref.theme;

import com.mucommander.text.Translator;
import com.mucommander.ui.chooser.FontChooser;
import com.mucommander.ui.dialog.pref.PreferencesDialog;
import com.mucommander.ui.layout.YBoxPanel;
import com.mucommander.ui.layout.ProportionalGridPanel;
import com.mucommander.ui.theme.ThemeData;
import com.mucommander.ui.chooser.PreviewLabel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * @author Nicolas Rinaudo, Maxence Bernard
 */
class FileEditorPanel extends ThemeEditorPanel implements PropertyChangeListener {
    // - Instance fields -----------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /** Used to preview the editor's theme. */
    private JTextArea preview;



    // - Initialisation ------------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Creates a new file table editor.
     * @param parent    dialog containing the panel.
     * @param themeData  themeData being edited.
     */
    public FileEditorPanel(PreferencesDialog parent, ThemeData themeData) {
        super(parent, Translator.get("theme_editor.editor_tab"), themeData);
        initUI();
    }



    // - UI initialisation ---------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Creates the JPanel that contains all of the color configuration elements.
     * @param fontChooser font chooser used by the editor panel.
     * @return the JPanel that contains all of the color configuration elements.
     */
    private JPanel createColorsPanel(FontChooser fontChooser) {
        ProportionalGridPanel gridPanel;   // Contains all the color buttons.
        JPanel                colorsPanel; // Used to wrap the colors panel in a flow layout.

        // Initialisation.
        gridPanel = new ProportionalGridPanel(3);

        // Header.
        addLabelRow(gridPanel, false);

        // Color buttons.
        addColorButtons(gridPanel, fontChooser, "theme_editor.normal",
                        ThemeData.EDITOR_FOREGROUND_COLOR, ThemeData.EDITOR_BACKGROUND_COLOR).addPropertyChangeListener(this);
        addColorButtons(gridPanel, fontChooser, "theme_editor.selected",
                        ThemeData.EDITOR_SELECTED_FOREGROUND_COLOR, ThemeData.EDITOR_SELECTED_BACKGROUND_COLOR).addPropertyChangeListener(this);

        // Wraps everything in a flow layout.
        colorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorsPanel.add(gridPanel);
        colorsPanel.setBorder(BorderFactory.createTitledBorder(Translator.get("theme_editor.colors")));

        return colorsPanel;
    }

    /**
     * Initialises the panel's UI.
     */
    private void initUI() {
        YBoxPanel   configurationPanel; // Contains all the configuration elements.
        FontChooser fontChooser;        // Used to select a font.
        JPanel      mainPanel;          // Main panel.

        // Font chooser and preview initialisation.
        mainPanel   = new JPanel(new BorderLayout());
        fontChooser = createFontChooser(ThemeData.EDITOR_FONT);
        mainPanel.add(createPreviewPanel(), BorderLayout.EAST);
        addFontChooserListener(fontChooser, preview);

        // Configuration panel initialisation.
        configurationPanel = new YBoxPanel();
        configurationPanel.add(fontChooser);
        configurationPanel.addSpace(10);
        configurationPanel.add(createColorsPanel(fontChooser));
        mainPanel.add(configurationPanel, BorderLayout.CENTER);

        // Layout.
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.NORTH);
    }

    /**
     * Creates the file editor preview panel.
     * @return the file editor preview panel.
     */
    private JPanel createPreviewPanel() {
        JPanel      panel;  // Preview panel.
        JScrollPane scroll; // Wraps the preview text are.

        // Initialises the preview text area.
        // Note that we do not need to set colors and fonts here, as this is all taken care
        // of by the color buttons and font chooser.
        preview = new JTextArea(15, 15);
        preview.setText(Translator.get("sample_text"));

        // Creates the panel.
        panel = new JPanel(new BorderLayout());
        panel.add(scroll = new JScrollPane(preview, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        scroll.getViewport().setPreferredSize(preview.getPreferredSize());
        panel.setBorder(BorderFactory.createTitledBorder(Translator.get("preview")));

        // Initialises colors.
        setBackgroundColors();
        setForegroundColors();

        return panel;
    }

    /**
     * Listens on changes on the foreground and background colors.
     */
    public void propertyChange(PropertyChangeEvent event) {
        // Background color changed.
        if(event.getPropertyName().equals(PreviewLabel.BACKGROUND_COLOR_PROPERTY_NAME))
            setBackgroundColors();

        // Foreground color changed.
        else if(event.getPropertyName().equals(PreviewLabel.FOREGROUND_COLOR_PROPERTY_NAME))
            setForegroundColors();
    }

    private void setBackgroundColors() {
        preview.setBackground(themeData.getColor(ThemeData.EDITOR_BACKGROUND_COLOR));
        preview.setSelectionColor(themeData.getColor(ThemeData.EDITOR_SELECTED_BACKGROUND_COLOR));
    }

    private void setForegroundColors() {
        preview.setForeground(themeData.getColor(ThemeData.EDITOR_FOREGROUND_COLOR));
        preview.setCaretColor(themeData.getColor(ThemeData.EDITOR_FOREGROUND_COLOR));
        preview.setSelectedTextColor(themeData.getColor(ThemeData.EDITOR_SELECTED_FOREGROUND_COLOR));
    }



    // - Modification management ---------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Ignored.
     */
    public void commit() {}
}
