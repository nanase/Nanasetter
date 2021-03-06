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
 * Created by nanase on 14/05/27.
 */

/**
 * ブロック、フォローとフォロー解除などの機能を提供するクラスです。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class RiskPorter extends Porter {
    /**
     * Twitter リストとプラグインホストを指定して新しい RiskPorter クラスのインスタンスを初期化します。
     *
     * @param twitterList Twitter リストを表す TwitterList オブジェクト。
     * @param pluginHost  プラグインホストを表す PluginHost オブジェクト。
     * @throws UnsatisfiedPermissionException プラグインで指定されていないパーミッションが要求されました。
     */
    RiskPorter(TwitterList twitterList, PluginHost pluginHost)
            throws UnsatisfiedPermissionException {
        super(twitterList, pluginHost);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PluginPermission getPermission() {
        return PluginPermission.RISK;
    }
}
