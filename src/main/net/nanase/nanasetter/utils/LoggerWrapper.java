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

public class LoggerWrapper {
    private final Logger logger;

    public LoggerWrapper(Logger logger) {
        this.logger = logger;
    }

    public void config(Object msg) {
        this.logger.config(msg.toString());
    }

    public void fine(Object msg) {
        this.logger.fine(msg.toString());
    }

    public void finer(Object msg) {
        this.logger.finer(msg.toString());
    }

    public void finest(Object msg) {
        this.logger.finest(msg.toString());
    }

    public void info(Object msg) {
        this.logger.info(msg.toString());
    }

    public void severe(Object msg) {
        this.logger.severe(msg.toString());
    }

    public void warning(Object msg) {
        this.logger.warning(msg.toString());
    }
}
