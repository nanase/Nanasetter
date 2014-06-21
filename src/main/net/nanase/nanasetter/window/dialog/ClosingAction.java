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
import net.nanase.nanasetter.utils.JSOUtils;
import netscape.javascript.JSObject;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.dialog.Dialog;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/19.
 */

class ClosingAction extends AbstractAction {

    public ClosingAction(String text, String type) {
        super(text);
        ButtonBar.setType(this, convertButtonType(type));
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

    public static Optional<Collection<ClosingAction>> parseFromJS(JSOUtils jsoUtils) {
        Object obj = jsoUtils.getJSObject().getMember("button");

        if (obj instanceof JSObject) {
            JSObject jsObject = (JSObject) obj;
            JSOUtils utils = new JSOUtils(jsObject);

            if (utils.hasMember("length")) {
                int length = utils.getNumber("length").orElse(0).intValue();
                Stream<String> strs = Stream.empty();

                for (int i = 0; i < length; i++)
                    strs = Stream.concat(strs, Stream.of((jsObject).getSlot(i).toString()));

                return Optional.of(strs.map(k -> new ClosingAction(getButtonDefaultText(k), k))
                        .collect(Collectors.toList()));
            } else {
                return Optional.of(Stream.of(utils.getMembersList())
                        .map(s -> new ClosingAction(utils.getString(s).orElse(getButtonDefaultText(s)), s))
                        .collect(Collectors.toList()));
            }
        } else if (obj instanceof String) {
            return Optional.of(Stream.of(((String) obj).split(""))
                    .map(k -> new ClosingAction(getButtonDefaultText(k), k))
                    .collect(Collectors.toList()));
        }

        return Optional.empty();
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
}
