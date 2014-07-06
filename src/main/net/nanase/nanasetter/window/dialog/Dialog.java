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

/**
 * 一つのオブジェクトをパラメータとして指定し、各種ダイアログ表示を行います。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class Dialog {
    private Window window;

    /**
     * 親ウィンドウを指定して新しい Dialog クラスのインスタンスを初期化します。
     *
     * @param window 親となる Window オブジェクト。
     */
    public Dialog(Window window) {
        this.window = window;

        // ControlsFXに対する暫定処理
        Translations.getTranslation("en").ifPresent(t -> Localization.setLocale(t.getLocale()));
    }

    /**
     * パラメータを指定して情報ダイアログを表示します。
     *
     * @param object ダイアログのパラメータ。
     */
    public void info(Object object) {
        if (object == null)
            return;

        if (object instanceof String)
            DialogImpl.info(this.window, (String) object);
        else if (object instanceof JSObject)
            DialogImpl.info(this.window, (JSObject) object);
        else
            DialogImpl.info(this.window, object.toString());
    }

    /**
     * パラメータを指定して確認ダイアログを表示し、押されたボタンに対応した文字列を返します。
     *
     * @param object ダイアログのパラメータ。
     * @return 押されたボタンに対応した文字列。
     */
    public String confirm(Object object) {
        if (object == null)
            return null;

        if (object instanceof String)
            return DialogImpl.confirm(this.window, (String) object);
        else if (object instanceof JSObject)
            return DialogImpl.confirm(this.window, (JSObject) object);
        else
            return DialogImpl.confirm(this.window, object.toString());
    }

    /**
     * パラメータを指定して入力ダイアログを表示し、テキストボックスに入力された文字列を返します。
     *
     * @param object ダイアログのパラメータ。
     * @return テキストボックスに入力された文字列。
     */
    public String input(Object object) {
        if (object == null)
            return null;

        if (object instanceof String)
            return DialogImpl.input(this.window, (String) object);
        else if (object instanceof JSObject)
            return DialogImpl.input(this.window, (JSObject) object);
        else
            return DialogImpl.input(this.window, object.toString());
    }

    /**
     * パラメータを指定して選択ダイアログを表示し、コンボボックスで選択された文字列を返します。
     *
     * @param object ダイアログのパラメータ。
     * @return コンボボックスで選択された文字列。
     */
    public String choice(Object object) {
        if (object == null)
            return null;

        if (object instanceof JSObject)
            return DialogImpl.choice(this.window, (JSObject) object);
        else
            return null;
    }
}
