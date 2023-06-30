package org.example.stronghold.context;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;

public class ClipboardUtils {
    public static void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit()
            .getSystemClipboard()
            .setContents(
                new StringSelection(text),
                null
            );
    }

    public static String pasteFromClipboard() throws Exception {
        return (String) Toolkit.getDefaultToolkit()
            .getSystemClipboard()
            .getContents(null)
            .getTransferData(DataFlavor.stringFlavor);
    }
}
