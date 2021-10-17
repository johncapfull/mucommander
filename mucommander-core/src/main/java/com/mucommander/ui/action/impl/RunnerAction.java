/*
 * This file is part of muCommander, http://www.mucommander.com
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


package com.mucommander.ui.action.impl;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.swing.KeyStroke;

import com.mucommander.commons.file.AbstractFile;
import com.mucommander.commons.file.FileOperation;
import com.mucommander.commons.file.archive.AbstractArchiveEntryFile;
import com.mucommander.commons.file.filter.AndFileFilter;
import com.mucommander.commons.file.filter.FileOperationFilter;
import com.mucommander.commons.file.filter.OrFileFilter;
import com.mucommander.commons.file.protocol.local.LocalFile;
import com.mucommander.core.desktop.DesktopManager;
import com.mucommander.text.Translator;
import com.mucommander.ui.action.AbstractActionDescriptor;
import com.mucommander.ui.action.ActionCategory;
import com.mucommander.ui.action.ActionDescriptor;
import com.mucommander.ui.action.ActionFactory;
import com.mucommander.ui.action.ActionProperties;
import com.mucommander.ui.action.MuAction;
import com.mucommander.ui.dialog.InformationDialog;
import com.mucommander.ui.main.MainFrame;
import com.mucommander.ui.main.table.FileTable;

/** Btnhub runner action */
public class RunnerAction extends SelectedFileAction {

    public RunnerAction(MainFrame mainFrame, Map<String,Object> properties) {
        super(mainFrame, properties);

        setSelectedFileFilter(
            new FileOperationFilter(FileOperation.READ_FILE)
        );
    }

    @Override
    public void performAction() {
        FileTable activeTable = mainFrame.getActiveTable();
        AbstractFile selectedFile = activeTable.getSelectedFile(false);

        if (selectedFile != null) {
            sendRunner("preview", "", selectedFile.getAbsolutePath());
        }
    }

    @Override
    public ActionDescriptor getDescriptor() {
        return new RunnerAction.Descriptor();
    }

    private void sendRunner(String cmd, String dir, String args) {
        try {
            PrintWriter writer = new PrintWriter("/tmp/btnhelper-runner", "UTF-8");
            writer.println(cmd);
            writer.println(dir);
            writer.println(args);
            writer.close();
        } catch (IOException ex) {
        }
    }

    public static class Factory implements ActionFactory {

        public MuAction createAction(MainFrame mainFrame, Map<String,Object> properties) {
            return new RunnerAction(mainFrame, properties);
        }
    }

    public static class Descriptor extends AbstractActionDescriptor {
        public static final String ACTION_ID = "FinderPreview";

        public String getId() { return ACTION_ID; }

        public ActionCategory getCategory() { return ActionCategory.FILES; }

        public KeyStroke getDefaultAltKeyStroke() { return null; }

        public KeyStroke getDefaultKeyStroke() { return KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_DOWN_MASK); }
    }
}
