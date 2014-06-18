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

package net.nanase.nanasetter.utils;

import netscape.javascript.JSObject;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/12.
 */

/**
 * JSObject の機能を拡張します。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class JSOUtils {
    private final JSObject jsObject;

    /**
     * JSObject を指定してインスタンスを初期化します。
     *
     * @param jsObject JSOUtils オブジェクトで使用される JSObject オブジェクト。
     */
    public JSOUtils(JSObject jsObject) {
        if (jsObject == null)
            throw new IllegalArgumentException();

        this.jsObject = jsObject;
    }

    /**
     * 指定されたプロパティが存在するときに、指定された Consumer 関数を実行します。
     *
     * @param name     プロパティ名。
     * @param consumer 実行される Consumer 関数。
     */
    public void ifExists(String name, Consumer<Object> consumer) {
        if (name == null)
            throw new IllegalArgumentException();

        if (consumer == null)
            throw new IllegalArgumentException();

        if (this.hasMember(name))
            consumer.accept(this.jsObject.getMember(name));
    }

    /**
     * 指定されたプロパティが存在するときに、指定された Consumer 関数を実行します。
     *
     * @param name     プロパティ名。
     * @param consumer 実行される Consumer 関数。
     */
    public void ifExistsAsBoolean(String name, Consumer<Boolean> consumer) {
        if (name == null)
            throw new IllegalArgumentException();

        if (consumer == null)
            throw new IllegalArgumentException();

        if (!this.hasMember(name))
            return;

        Object obj = this.jsObject.getMember(name);

        if (obj instanceof Boolean)
            consumer.accept((Boolean) obj);
    }

    /**
     * 指定されたプロパティが存在するときに、指定された Consumer 関数を実行します。
     *
     * @param name     プロパティ名。
     * @param consumer 実行される Consumer 関数。
     */
    public void ifExistsAsString(String name, Consumer<String> consumer) {
        if (name == null)
            throw new IllegalArgumentException();

        if (consumer == null)
            throw new IllegalArgumentException();

        if (!this.hasMember(name))
            return;

        Object obj = this.jsObject.getMember(name);

        if (obj instanceof String)
            consumer.accept((String) obj);
    }

    /**
     * 指定されたプロパティが存在するときに、指定された Consumer 関数を実行します。
     *
     * @param name     プロパティ名。
     * @param consumer 実行される Consumer 関数。
     */
    public void ifExistsAsNumber(String name, Consumer<Number> consumer) {
        if (name == null)
            throw new IllegalArgumentException();

        if (consumer == null)
            throw new IllegalArgumentException();

        if (!this.hasMember(name))
            return;

        Object obj = this.jsObject.getMember(name);

        if (obj instanceof Number)
            consumer.accept((Number) obj);
    }

    /**
     * 指定されたプロパティが存在するならばそのプロパティを返します。
     * プロパティが存在しない、型が一致しない場合は Optional.empty() と同値を返します。
     *
     * @param name プロパティ名。
     * @return プロパティの値を内包した Optional&lt;Boolean&gt; オブジェクト。
     */
    public Optional<Boolean> getBoolean(String name) {
        if (name == null)
            throw new IllegalArgumentException();

        if (!this.hasMember(name))
            return Optional.empty();

        Object obj = this.jsObject.getMember(name);

        if (obj instanceof Boolean)
            return Optional.of((Boolean) obj);
        else
            return Optional.empty();
    }

    /**
     * 指定されたプロパティが存在するならばそのプロパティを返します。
     * プロパティが存在しない、型が一致しない場合は Optional.empty() と同値を返します。
     *
     * @param name プロパティ名。
     * @return プロパティの値を内包した Optional&lt;String&gt; オブジェクト。
     */
    public Optional<String> getString(String name) {
        if (name == null)
            throw new IllegalArgumentException();

        if (!this.hasMember(name))
            return Optional.empty();

        Object obj = this.jsObject.getMember(name);

        if (obj instanceof String)
            return Optional.of((String) obj);
        else
            return Optional.empty();
    }

    /**
     * 指定されたプロパティが存在するならばそのプロパティを返します。
     * プロパティが存在しない、型が一致しない場合は Optional.empty() と同値を返します。
     *
     * @param name プロパティ名。
     * @return プロパティの値を内包した Optional&lt;Number&gt; オブジェクト。
     */
    public Optional<Number> getNumber(String name) {
        if (name == null)
            throw new IllegalArgumentException();

        if (!this.hasMember(name))
            return Optional.empty();

        Object obj = this.jsObject.getMember(name);

        if (obj instanceof Number)
            return Optional.of((Number) obj);
        else
            return Optional.empty();
    }

    /**
     * 指定したプロパティ名を持つプロパティが存在するかどうかの真偽値を返します。
     *
     * @param name プロパティ名。
     * @return プロパティが存在するとき true、存在しないとき false。
     */
    public boolean hasMember(String name) {
        if (name == null)
            throw new IllegalArgumentException();

        try {
            //return !this.getTypeString(name).equals("undefined");
            return (boolean) this.jsObject.eval("typeof this." + name + " !== 'undefined'");
        } catch (netscape.javascript.JSException e) {
            return false;
        }
    }

    /**
     * 指定したプロパティのJavaScriptでの型を取得します。
     *
     * @param name プロパティ名。
     * @return JavaScriptでの型名。
     */
    public String getTypeString(String name) {
        if (name == null)
            throw new IllegalArgumentException();

        return (String) this.jsObject.eval("typeof this." + name);
    }
}
