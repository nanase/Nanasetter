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

import impl.org.controlsfx.i18n.Localization;
import javafx.event.ActionEvent;
import netscape.javascript.JSObject;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.dialog.Dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.nanase.nanasetter.utils.JSObjectUtils.*;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/19.
 */

class ClosingAction extends AbstractAction {

    private final String buttonTypeString;

    public ClosingAction(String text, String type) {
        super(text);
        ButtonBar.setType(this, convertButtonType(type));
        this.buttonTypeString = getButtonTypeString(type);
    }

    /**
     * Invoked when a specific event of the type for which this handler is
     * registered happens.
     *
     * @param event the event which occurred
     */
    @Override
    public void handle(ActionEvent event) {
        ((Dialog) event.getSource()).hide();
    }

    @Override
    public String toString() {
        return this.buttonTypeString;
    }

    public static List<ClosingAction> parseFromJS(JSObject jsObject) {
        Object obj = getMember(jsObject, "button", Object.class).orElse(null);

        List<ClosingAction> res = new ArrayList<>();

        if (obj instanceof JSObject) {
            JSObject buttonObject = (JSObject) obj;

            if (isArray(buttonObject))
                getArray(buttonObject, String.class)
                        .ifPresent(s -> res.addAll(s.stream()
                                .map(k -> new ClosingAction(getButtonDefaultText(k), k))
                                .collect(Collectors.toList())));
            else
                getMembersList(buttonObject)
                        .ifPresent(m -> res.addAll(Stream.of(m)
                                .map(s -> new ClosingAction(getMember(buttonObject, s, String.class)
                                        .orElse(getButtonDefaultText(s)), s))
                                .collect(Collectors.toList())));
        } else if (obj instanceof String) {
            res.addAll(Stream.of(((String) obj).split(""))
                    .map(k -> new ClosingAction(getButtonDefaultText(k), k))
                    .collect(Collectors.toList()));
        }

        return res;
    }

    private static ButtonBar.ButtonType convertButtonType(String name) {
        switch (name.toLowerCase()) {
            case "y":
            case "yes":
                return ButtonBar.ButtonType.YES;

            case "n":
            case "no":
                return ButtonBar.ButtonType.NO;

            case "c":
            case "cancel":
                return ButtonBar.ButtonType.CANCEL_CLOSE;

            case "o":
            case "ok":
                return ButtonBar.ButtonType.OK_DONE;

            default:
                return ButtonBar.ButtonType.CANCEL_CLOSE;
        }
    }

    private static String getButtonDefaultText(String name) {
        switch (name.toLowerCase()) {
            case "y":
            case "yes":
                return Localization.getString("dlg.yes.button");

            case "n":
            case "no":
                return Localization.getString("dlg.no.button");

            case "c":
            case "cancel":
                return Localization.getString("dlg.cancel.button");

            case "o":
            case "ok":
                return Localization.getString("dlg.ok.button");

            default:
                return name;
        }
    }

    private static String getButtonTypeString(String name) {
        switch (name.toLowerCase()) {
            case "y":
            case "yes":
                return "yes";

            case "n":
            case "no":
                return "no";

            case "c":
            case "cancel":
                return "cancel";

            case "o":
            case "ok":
                return "ok";

            default:
                return "unknown";
        }
    }
}
