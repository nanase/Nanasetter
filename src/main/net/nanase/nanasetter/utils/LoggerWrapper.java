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

import java.util.logging.Logger;

/**
 * Project: Nanasetter
 * Created by nanase on 14/08/12.
 */

/**
 * JavaScript からロガーを扱うために必要なメソッドを提供します。
 */
public class LoggerWrapper {
    private final Logger logger;

    /**
     * 本体となるロガーを指定して新しい LoggerWrapper クラスのインスタンスを初期化します。
     *
     * @param logger 本体となる Logger オブジェクト。
     */
    public LoggerWrapper(Logger logger) {
        if (logger == null)
            throw new IllegalArgumentException();

        this.logger = logger;
    }

    /**
     * レベル CONFIG としてロギングします。
     *
     * @param msg メッセージとなる文字列またはその他のオブジェクト。
     */
    public void config(Object msg) {
        if (msg == null)
            throw new IllegalArgumentException();

        this.logger.config(msg.toString());
    }

    /**
     * レベル FINE としてロギングします。
     *
     * @param msg メッセージとなる文字列またはその他のオブジェクト。
     */
    public void fine(Object msg) {
        if (msg == null)
            throw new IllegalArgumentException();

        this.logger.fine(msg.toString());
    }

    /**
     * レベル FINER としてロギングします。
     *
     * @param msg メッセージとなる文字列またはその他のオブジェクト。
     */
    public void finer(Object msg) {
        if (msg == null)
            throw new IllegalArgumentException();

        this.logger.finer(msg.toString());
    }

    /**
     * レベル FINEST としてロギングします。
     *
     * @param msg メッセージとなる文字列またはその他のオブジェクト。
     */
    public void finest(Object msg) {
        if (msg == null)
            throw new IllegalArgumentException();

        this.logger.finest(msg.toString());
    }

    /**
     * レベル INFO としてロギングします。
     *
     * @param msg メッセージとなる文字列またはその他のオブジェクト。
     */
    public void info(Object msg) {
        if (msg == null)
            throw new IllegalArgumentException();

        this.logger.info(msg.toString());
    }

    /**
     * レベル SEVERE としてロギングします。
     *
     * @param msg メッセージとなる文字列またはその他のオブジェクト。
     */
    public void severe(Object msg) {
        if (msg == null)
            throw new IllegalArgumentException();

        this.logger.severe(msg.toString());
    }

    /**
     * レベル WARNING としてロギングします。
     *
     * @param msg メッセージとなる文字列またはその他のオブジェクト。
     */
    public void warning(Object msg) {
        if (msg == null)
            throw new IllegalArgumentException();
        
        this.logger.warning(msg.toString());
    }
}
