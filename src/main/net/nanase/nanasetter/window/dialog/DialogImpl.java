/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Tomona Nanase
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.nanase.nanasetter.window.dialog;

import javafx.stage.Window;
import net.nanase.nanasetter.utils.JSObjectUtils;
import netscape.javascript.JSObject;
import org.controlsfx.dialog.Dialogs;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/27.
 */

class DialogImpl {
    public static void info(Window window, String message) {
        if (message == null)
            return;

        Dialogs.create()
                .owner(window)
                .title("")
                .message(message)
                .showInformation();
    }

    public static void info(Window window, JSObject parameters) {
        if (parameters == null)
            return;

        Dialogs dialogs = getDefaultDialogs(window, parameters);
        String type = JSObjectUtils.getMember(parameters, "type", String.class).orElse("info");

        switch (type) {
            case "warning":
            case "warn":
                dialogs.showWarning();
                break;

            case "error":
                dialogs.showError();
                break;

            default:
                dialogs.showInformation();
                break;
        }
    }

    public static String confirm(Window window, String message) {
        return Dialogs.create()
                .owner(window)
                .title("")
                .message(message)
                .showConfirm()
                .toString()
                .toLowerCase();
    }

    public static String confirm(Window window, JSObject parameters) {
        if (parameters == null)
            return null;

        return getDefaultDialogs(window, parameters)
                .showConfirm()
                .toString()
                .toLowerCase();
    }

    public static String input(Window window, String message) {
        return Dialogs.create()
                .owner(window)
                .title("")
                .message(message)
                .showTextInput()
                .orElse(null);
    }

    public static String input(Window window, JSObject parameters) {
        if (parameters == null)
            return null;

        Dialogs dialogs = getDefaultDialogs(window, parameters);
        String defaultText = JSObjectUtils.getMember(parameters, "text", String.class).orElse("");

        return dialogs.showTextInput(defaultText)
                .orElse(null);
    }

    private static Dialogs getDefaultDialogs(Window window, JSObject parameters) {
        Dialogs dialogs = Dialogs.create().owner(window).title("");

        JSObjectUtils.ifExists(parameters, "message", String.class, dialogs::message);
        JSObjectUtils.ifExists(parameters, "masthead", String.class, dialogs::masthead);
        JSObjectUtils.ifExists(parameters, "title", String.class, dialogs::title);

        if (JSObjectUtils.hasMember(parameters, "button"))
            dialogs.actions(ClosingAction.parseFromJS(parameters));

        return dialogs;
    }
}
