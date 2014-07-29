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

/**
 * プラグインがななせったーにアクセスするための機能を提供します。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class PluginHost {
    private final Plugin plugin;
    private final Logger logger;
    private final TwitterList twitterList;
    private final Dialog dialog;

    /**
     * プラグインなどのパラメータを元に、新しい PluginHost クラスのインスタンスを初期化します。
     *
     * @param plugin      プラグイン。
     * @param twitterList Twitter オブジェクトが格納された TwitterList オブジェクト。
     * @param dialog      Dialog オブジェクト。
     */
    public PluginHost(Plugin plugin, TwitterList twitterList, Dialog dialog) {
        if (plugin == null)
            throw new IllegalArgumentException();

        if (twitterList == null)
            throw new IllegalArgumentException();

        if (dialog == null)
            throw new IllegalArgumentException();

        this.plugin = plugin;
        this.logger = Logger.getLogger("nanasetter." + plugin.getName());
        this.twitterList = twitterList;
        this.dialog = dialog;
    }

    /**
     * READ_REST パーミッションで実行できる機能を提供するポーターオブジェクトを取得します。
     *
     * @return ReadRESTPorter オブジェクト。
     * @throws UnsatisfiedPermissionException プラグインで指定されていないパーミッションが要求されました。
     */
    public ReadRESTPorter getReadREST() throws UnsatisfiedPermissionException {
        return new ReadRESTPorter(this.twitterList, this);
    }

    /**
     * WRITE パーミッションで実行できる機能を提供するポーターオブジェクトを取得します。
     *
     * @return WritePorter オブジェクト。
     * @throws UnsatisfiedPermissionException プラグインで指定されていないパーミッションが要求されました。
     */
    public WritePorter getWrite() throws UnsatisfiedPermissionException {
        return new WritePorter(this.twitterList, this);
    }

    /**
     * READ_STREAMING パーミッションで実行できる機能を提供するポーターオブジェクトを取得します。
     *
     * @return ReadStreamingPorter オブジェクト。
     * @throws UnsatisfiedPermissionException プラグインで指定されていないパーミッションが要求されました。
     */
    public ReadStreamingPorter getReadStreaming() throws UnsatisfiedPermissionException {
        return new ReadStreamingPorter(this.twitterList, this);
    }

    /**
     * EXTEND パーミッションで実行できる機能を提供するポーターオブジェクトを取得します。
     *
     * @return ExtendPorter オブジェクト。
     * @throws UnsatisfiedPermissionException プラグインで指定されていないパーミッションが要求されました。
     */
    public ExtendPorter getExtend() throws UnsatisfiedPermissionException {
        return new ExtendPorter(this.twitterList, this);
    }

    /**
     * CONFIGURE パーミッションで実行できる機能を提供するポーターオブジェクトを取得します。
     *
     * @return ConfigurePorter オブジェクト。
     * @throws UnsatisfiedPermissionException プラグインで指定されていないパーミッションが要求されました。
     */
    public ConfigurePorter getConfigure() throws UnsatisfiedPermissionException {
        return new ConfigurePorter(this.twitterList, this);
    }

    /**
     * ACCESS_DIRECT_MESSAGE パーミッションで実行できる機能を提供するポーターオブジェクトを取得します。
     *
     * @return AccessDirectMessagePorter オブジェクト。
     * @throws UnsatisfiedPermissionException プラグインで指定されていないパーミッションが要求されました。
     */
    public AccessDirectMessagePorter getAccessDirectMessage() throws UnsatisfiedPermissionException {
        return new AccessDirectMessagePorter(this.twitterList, this);
    }

    /**
     * RISK パーミッションで実行できる機能を提供するポーターオブジェクトを取得します。
     *
     * @return RiskPorter オブジェクト。
     * @throws UnsatisfiedPermissionException プラグインで指定されていないパーミッションが要求されました。
     */
    public RiskPorter getRisk() throws UnsatisfiedPermissionException {
        return new RiskPorter(this.twitterList, this);
    }

    /**
     * Dialog オブジェクトを取得します。
     *
     * @return Dialog オブジェクト。
     */
    public Dialog getDialog() {
        return this.dialog;
    }

    /**
     * Plugin オブジェクトを取得します。
     *
     * @return このプラグインホストに割り当てられた Plugin オブジェクト。
     */
    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Logger オブジェクトを取得します。
     *
     * @return このプラグインホストに割り当てられた Logger オブジェクト。
     */
    public Logger getLogger() {
        return this.logger;
    }

    /**
     * TwitterList オブジェクトを取得します。
     *
     * @return TwitterList オブジェクト。
     */
    public TwitterList getTwitterList() {
        return this.twitterList;
    }
}
