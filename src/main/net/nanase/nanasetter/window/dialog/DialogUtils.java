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
import impl.org.controlsfx.i18n.Translations;
import javafx.stage.Window;
import net.nanase.nanasetter.utils.JSOUtils;
import netscape.javascript.JSObject;
import org.controlsfx.dialog.Dialogs;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/08.
 */

public class DialogUtils {

    private Window window;

    public DialogUtils(Window window) {
        this.window = window;

        // ControlsFXに対する暫定処理
        Translations.getTranslation("en").ifPresent(t -> Localization.setLocale(t.getLocale()));
    }

    public void showMessage(Object object) {
        if (object == null)
            return;

        if (object instanceof String)
            this.showMessage((String) object);
        else if (object instanceof JSObject)
            this.showMessage((JSObject) object);
        else
            this.showMessage(object.toString());
    }

    private void showMessage(String message) {
        if (message == null)
            return;

        Dialogs.create()
                .owner(this.window)
                .title("")
                .message(message)
                .showInformation();
    }

    private void showMessage(JSObject parameters) {
        if (parameters == null)
            return;

        JSOUtils jsObject = new JSOUtils(parameters);
        Dialogs dialogs = Dialogs.create().owner(this.window).title("");

        jsObject.ifExistsAsString("message", dialogs::message);
        jsObject.ifExistsAsString("masthead", dialogs::masthead);
        jsObject.ifExistsAsString("title", dialogs::title);

        if (jsObject.hasMember("button"))
            ClosingAction.parseFromJS(jsObject).ifPresent(dialogs::actions);

        String type = jsObject.getString("type").orElse("info");

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

    public String confirm(Object object) {
        if (object == null)
            return null;

        if (object instanceof String)
            return this.confirm((String) object);
        else if (object instanceof JSObject)
            return this.confirm((JSObject) object);
        else
            return this.confirm(object.toString());
    }

    private String confirm(String message) {
        return Dialogs.create()
                .owner(this.window)
                .title("")
                .message(message)
                .showConfirm()
                .toString()
                .toLowerCase();
    }

    private String confirm(JSObject parameters) {
        if (parameters == null)
            return null;

        JSOUtils jsObject = new JSOUtils(parameters);
        Dialogs dialogs = Dialogs.create().owner(this.window).title("");

        jsObject.ifExistsAsString("message", dialogs::message);
        jsObject.ifExistsAsString("masthead", dialogs::masthead);
        jsObject.ifExistsAsString("title", dialogs::title);

        if (jsObject.hasMember("button"))
            ClosingAction.parseFromJS(jsObject).ifPresent(dialogs::actions);

        return dialogs.showConfirm().toString().toLowerCase();
    }
}
