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

import java.util.EnumSet;

/**
 * Project: Nanasetter
 * Created by nanase on 14/05/15.
 */

/**
 * プラグインで利用できる Twitter 機能を区分するためのパーミッション(権限)を表現する列挙体です。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public enum PluginPermission {
    /**
     * 読み取り権限。
     * タイムラインの読み取り、リストの読み取り、ツイートの表示などが可能です。
     */
    read,

    /**
     * ストリーム権限。
     * ストリーム通知の受け取りが可能です。
     */
    stream,

    /**
     * 書き込み権限。
     * ツイート送信と削除、リツイート、お気に入り、リストの変更、プロフィールの変更などが可能です。
     */
    write,

    /**
     * ダイレクトメッセージ権限。
     * ダイレクトメッセージの読み込み、送信、削除が可能です。
     */
    directMessage,

    /**
     * クリティカル権限。
     * ブロック、フォローとフォロー解除などが可能です。
     */
    critical,;

    /**
     * PluginPermission 列挙体に属する列挙値すべてを含んだ EnumSet オブジェクトを表します。
     * この権限を持つプラグインはすべての Twitter 機能を使用します。
     */
    public final static EnumSet<PluginPermission> full = EnumSet.allOf(PluginPermission.class);

    /**
     * PluginPermission 列挙体に属する列挙値の何れも含まない EnumSet オブジェクトを表します。
     * この権限を持つプラグインは一切の Twitter 機能を使用しません。
     */
    public final static EnumSet<PluginPermission> none = EnumSet.noneOf(PluginPermission.class);
}
