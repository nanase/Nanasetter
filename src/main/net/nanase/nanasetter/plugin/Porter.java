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

/**
 * Project: Nanasetter
 * Created by nanase on 14/05/24.
 */

/**
 * API をラップし、実行をログに出力するための抽象クラスです。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public abstract class Porter {

    protected final PluginHost pluginHost;

    protected final TwitterList twitterList;

    public Porter(TwitterList twitterList, PluginHost pluginHost) throws UnsatisfiedPermissionException {
        if (twitterList == null)
            throw new IllegalArgumentException();

        if (pluginHost == null)
            throw new IllegalArgumentException();

        if(!pluginHost.getPlugin().getPermission().contains(this.getPermission()))
            throw new UnsatisfiedPermissionException(this.getPermission());

        this.twitterList = twitterList;
        this.pluginHost = pluginHost;
    }

    protected abstract PluginPermission getPermission();
}
