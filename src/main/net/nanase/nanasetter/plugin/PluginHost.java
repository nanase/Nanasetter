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

import net.nanase.nanasetter.twitter.TwitterList;
import net.nanase.nanasetter.window.dialog.Dialog;

import java.util.logging.Logger;

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/01.
 */

public class PluginHost {

    private final Plugin plugin;

    private final Logger logger;

    private final TwitterList twitterList;

    public PluginHost(Plugin plugin, TwitterList twitterList) {
        this.plugin = plugin;
        this.logger = Logger.getLogger("nanasetter." + plugin.getName());
        this.twitterList = twitterList;
    }

    public ReadRESTPorter getReadREST() throws UnsatisfiedPermissionException {
        return new ReadRESTPorter(this.twitterList, this);
    }

    public WritePorter getWrite()throws UnsatisfiedPermissionException {
        return new WritePorter(this.twitterList, this);
    }

    public ReadStreamingPorter getReadStreaming() throws UnsatisfiedPermissionException {
        return new ReadStreamingPorter(this.twitterList, this);
    }

    public ExtendPorter getExtend() throws UnsatisfiedPermissionException {
        return new ExtendPorter(this.twitterList, this);
    }

    public ConfigurePorter getConfigure() throws UnsatisfiedPermissionException {
        return new ConfigurePorter(this.twitterList, this);
    }

    public AccessDirectMessagePorter getAccessDirectMessage()throws UnsatisfiedPermissionException {
        return new AccessDirectMessagePorter(this.twitterList, this);
    }

    public RiskPorter getRisk() throws UnsatisfiedPermissionException {
        return new RiskPorter(this.twitterList, this);
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

    public TwitterList getTwitterList() {
        return this.twitterList;
    }
}
