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

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/22.
 */

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * JSObject の機能を拡張します。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class JSObjectUtils {
    public static boolean hasMember(JSObject object, String name) {
        if (object == null)
            return false;

        if (name == null)
            return false;

        try {
            return (boolean) object.eval("typeof this." + name + " !== 'undefined'");
        } catch (JSException e) {
            return false;
        }
    }

    public static String getTypeString(JSObject object, String name) {
        if (object == null)
            return "undefined";

        if (name == null)
            return "undefined";

        try {
            if (hasMember(object, name))
                return (String) object.eval("typeof this." + name);
        } catch (JSException e) {
        }

        return "undefined";
    }

    public static Optional<String[]> getMembersList(JSObject object) {
        if (object == null)
            return Optional.empty();

        try {
            return Optional.of(((String) object.eval("Object.keys(this).toString()")).split(","));
        } catch (JSException e) {
        }

        return Optional.empty();
    }

    public static <T> void ifExists(JSObject object, String name, Class<T> tClass, Consumer<T> consumer) {
        if (object == null)
            return;

        if (name == null)
            return;

        if (consumer == null)
            return;

        ifExists(object, name, tClass, consumer, null);
    }

    public static <T> void ifExists(JSObject object, String name, Class<T> tClass, Consumer<T> consumer, Runnable runnable) {
        if (object == null)
            return;

        if (name == null)
            return;

        Optional<T> member = getMember(object, name, tClass);

        if (member.isPresent() && consumer != null)
            consumer.accept(member.get());
        else if (runnable != null)
            runnable.run();
    }

    public static boolean isArray(JSObject object) {
        if (object == null)
            return false;

        return hasMember(object, "length");
    }

    public static boolean isArray(JSObject object, String name) {
        if (object == null)
            return false;

        if (name == null)
            return false;

        return hasMember(object, name + ".length");
    }

    public static <T> Optional<T> getMember(JSObject object, String name, Class<T> tClass) {
        if (tClass == null)
            return Optional.empty();

        if (!hasMember(object, name))
            return Optional.empty();

        Object obj = object.getMember(name);

        if (tClass.isInstance(obj))
            return Optional.of(tClass.cast(obj));

        return Optional.empty();
    }
}
