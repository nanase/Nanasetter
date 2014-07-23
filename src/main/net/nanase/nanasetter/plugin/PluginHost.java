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

package net.nanase.nanasetter.plugin;

import net.nanase.nanasetter.window.dialog.Dialog;

import java.util.logging.Logger;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/01.
 */

public class PluginHost {

    private final Plugin plugin;

    private final Logger logger;

    public PluginHost(Plugin plugin) {
        this.plugin = plugin;
        this.logger = Logger.getLogger("nanasetter." + plugin.getName());
    }

    public ReadRESTPorter getReadREST() {
        // stub
        return null;
    }

    public WritePorter getWrite() {
        // stub
        return null;
    }

    public ReadStreamingPorter getReadStreaming() {
        // stub
        return null;
    }

    public ExtendPorter getExtend() {
        // stub
        return null;
    }

    public ConfigurePorter getConfigure() {
        // stub
        return null;
    }

    public AccessDirectMessagePorter getAccessDirectMessage() {
        // stub
        return null;
    }

    public RiskPorter getRisk() {
        // stub
        return null;
    }

    public Dialog getDialog() {
        // stub
        return null;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
