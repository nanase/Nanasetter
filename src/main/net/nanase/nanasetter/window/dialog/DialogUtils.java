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
import netscape.javascript.JSObject;

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
            DialogUtilsImpl.showMessage(this.window, (String) object);
        else if (object instanceof JSObject)
            DialogUtilsImpl.showMessage(this.window, (JSObject) object);
        else
            DialogUtilsImpl.showMessage(this.window, object.toString());
    }

    public String confirm(Object object) {
        if (object == null)
            return null;

        if (object instanceof String)
            return DialogUtilsImpl.confirm(this.window, (String) object);
        else if (object instanceof JSObject)
            return DialogUtilsImpl.confirm(this.window, (JSObject) object);
        else
            return DialogUtilsImpl.confirm(this.window, object.toString());
    }
}
