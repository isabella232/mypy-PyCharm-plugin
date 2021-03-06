package com.dropbox.plugins.mypy_plugin.actions;

import com.dropbox.plugins.mypy_plugin.MypyTerminal;
import com.dropbox.plugins.mypy_plugin.MypyToolWindowFactory;
import com.dropbox.plugins.mypy_plugin.model.MypyError;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

final class CopyError extends AnAction implements DumbAware {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        ToolWindow tw = ToolWindowManager.getInstance(project).getToolWindow(
                MypyToolWindowFactory.MYPY_PLUGIN_ID);
        if (!tw.isVisible()) {
            return;
        }
        MypyTerminal terminal = MypyToolWindowFactory.getMypyTerminal(project);
        if (terminal == null) {
            return;
        }
        if (terminal.getRunner().isRunning()) {
            return;
        }
        MypyError error = terminal.getErrorsList().getSelectedValue();
        if (error == null) { // no errors
            return;
        }
        if (error.getLevel() == MypyError.HEADER) {
            return;
        }
        StringSelection selection = new StringSelection(error.getRaw());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}