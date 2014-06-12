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

import java.util.function.Consumer;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/12.
 */

public class JSOUtils {
    private final JSObject jsObject;

    public JSOUtils(JSObject jsObject) {
        this.jsObject = jsObject;
    }

    public void ifExists(String name, Consumer<Object> consumer) {
        if (this.hasMember(name))
            consumer.accept(this.jsObject.getMember(name));
    }

    public void ifExistsAsString(String name, Consumer<String> consumer) {
        if (!this.hasMember(name))
            return;

        Object obj = this.jsObject.getMember(name);

        if (obj instanceof String)
            consumer.accept((String) obj);
    }

    public void ifExistsAsNumber(String name, Consumer<Number> consumer) {
        if (!this.hasMember(name))
            return;

        Object obj = this.jsObject.getMember(name);

        if (obj instanceof Number)
            consumer.accept((Number) obj);
    }

    public boolean hasMember(String name) {
        return !this.getTypeString(name).equals("undefined");
        //return (boolean) this.jsObject.eval("typeof this." + name + " !== 'undefined'");
    }

    public String getTypeString(String name) {
        return (String) this.jsObject.eval("typeof this." + name);
    }
}
