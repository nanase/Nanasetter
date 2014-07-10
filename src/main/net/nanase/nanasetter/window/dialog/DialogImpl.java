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
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/27.
 */

class DialogImpl {
    public static void info(Window window, String message) {
        if (message == null)
            return;

        createDefaultDialogs(window)
                .message(message)
                .showInformation();
    }

    public static void info(Window window, JSObject parameters) {
        if (parameters == null)
            return;

        Dialogs dialogs = createDefaultDialogs(window);
        applyParameter(dialogs, parameters);

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
        return createDefaultDialogs(window)
                .message(message)
                .showConfirm()
                .toString()
                .toLowerCase();
    }

    public static String confirm(Window window, JSObject parameters) {
        if (parameters == null)
            return null;

        Dialogs dialogs = createDefaultDialogs(window);
        applyParameter(dialogs, parameters);

        return dialogs.showConfirm()
                .toString()
                .toLowerCase();
    }

    public static String input(Window window, String message) {
        return createDefaultDialogs(window)
                .message(message)
                .showTextInput()
                .orElse(null);
    }

    public static String input(Window window, JSObject parameters) {
        if (parameters == null)
            return null;

        Dialogs dialogs = createDefaultDialogs(window);
        applyParameter(dialogs, parameters);

        String defaultText = JSObjectUtils.getMember(parameters, "text", String.class).orElse("");

        return dialogs.showTextInput(defaultText)
                .orElse(null);
    }

    public static String choice(Window window, JSObject parameters) {
        if (parameters == null)
            return null;

        Dialogs dialogs = createDefaultDialogs(window);
        applyParameter(dialogs, parameters);

        Collection<String> choices;
        String defaultChoice = null;

        if (JSObjectUtils.isArray(parameters))
            choices = JSObjectUtils.getArray(parameters, String.class).collect(Collectors.toList());
        else if (JSObjectUtils.isArray(parameters, "choices")) {
            choices = JSObjectUtils.getArray(parameters, "choices", String.class).collect(Collectors.toList());
            defaultChoice = JSObjectUtils.getMember(parameters, "defaultChoice", String.class).orElse(null);
        } else
            return null;

        if (defaultChoice == null)
            return dialogs.showChoices(choices).orElse(null);
        else
            return dialogs.showChoices(defaultChoice, choices).orElse(null);
    }

    public static String command(Window window, JSObject parameters) {
        if (parameters == null)
            return null;

        Dialogs dialogs = createDefaultDialogs(window);
        applyParameter(dialogs, parameters);

        Action action = dialogs.showCommandLinks(convertToCommandLink(parameters).collect(Collectors.toList()));

        if (Dialog.Actions.CANCEL.equals(action))
            return null;
        else
            return action.textProperty().get();
    }

    private static Stream<Dialogs.CommandLink> convertToCommandLink(JSObject parameters) {
        if (!JSObjectUtils.hasMember(parameters, "commands"))
            return Stream.empty();

        JSObject commands = JSObjectUtils.getMember(parameters, "commands", JSObject.class).get();


        if (JSObjectUtils.isArray(commands)) {
            // for String array
            return JSObjectUtils.getArray(commands, String.class)
                    .map(t -> new Dialogs.CommandLink(t, null));
        } else {
            // for JSObject array
            return JSObjectUtils.getMembersList(commands)
                    .map(m -> new Dialogs.CommandLink(m, JSObjectUtils.getMember(commands, m, String.class)
                            .orElse(null)));
        }
    }

    private static void applyParameter(Dialogs dialogs, JSObject parameters) {
        JSObjectUtils.ifExists(parameters, "message", String.class, dialogs::message);
        JSObjectUtils.ifExists(parameters, "masthead", String.class, dialogs::masthead);
        JSObjectUtils.ifExists(parameters, "title", String.class, dialogs::title);

        if (JSObjectUtils.hasMember(parameters, "button"))
            dialogs.actions(ClosingAction.parseFromJS(parameters).collect(Collectors.toList()));
    }

    private static Dialogs createDefaultDialogs(Window window) {
        return Dialogs.create()
                .owner(window)
                .title(Dialogs.USE_DEFAULT)
                        //.message(Dialogs.USE_DEFAULT)
                        //.masthead(Dialogs.USE_DEFAULT)
                        //.lightweight()
                .style(DialogStyle.CROSS_PLATFORM_DARK);
    }
}
