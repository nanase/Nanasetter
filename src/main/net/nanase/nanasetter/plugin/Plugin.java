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

import net.nanase.nanasetter.utils.JSObjectUtils;
import net.nanase.nanasetter.utils.Version;
import netscape.javascript.JSObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

/**
 * Project: Nanasetter
 * Created by nanase on 14/05/14.
 */

/**
 * プラグインの情報を格納するクラスです。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class Plugin {
    private static final Pattern namePattern;

    static {
        namePattern = Pattern.compile("^[\\w]+$");
    }

    private final String name;

    private final String author;

    private final URL siteAddress;

    private final Version version;

    private final EnumSet<PluginPermission> permission;

    private Plugin(String name,
                   String author,
                   URL siteAddress,
                   Version version,
                   EnumSet<PluginPermission> permission) {
        this.name = name;
        this.author = author;
        this.siteAddress = siteAddress;
        this.version = version;
        this.permission = permission;
    }

    /**
     * 指定された JSObject オブジェクトから Plugin クラスのインスタンスを生成します。
     *
     * @param jsObject 読み込まれる JSObject オブジェクト。
     * @return Plugin オブジェクト。
     * @throws NoSuchElementException   指定された JSObject オブジェクトに、必要なプロパティが格納されていない、
     *                                  または読み取れない場合に発生します。
     * @throws IllegalArgumentException 指定された JSObject オブジェクトに不正な値が格納されていた場合に発生します。
     */
    public static Plugin create(JSObject jsObject) throws NoSuchElementException, IllegalArgumentException {
        String name;
        String author;
        URL siteAddress;
        Version version;
        EnumSet<PluginPermission> permissionFlag;

        name = JSObjectUtils.getMember(jsObject, "name", String.class).get();
        author = JSObjectUtils.getMember(jsObject, "author", String.class).get();
        version = Version.parse(JSObjectUtils.getMember(jsObject, "version", String.class).get());

        if (!checkNameString(name))
            throw new IllegalArgumentException(String.format("'%s' は無効なプラグイン名です。", name));

        try {
            String siteAddressString = JSObjectUtils.getMember(jsObject, "siteAddress", String.class).orElse(null);
            siteAddress = (siteAddressString == null) ? null : new URL(siteAddressString);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("サイトアドレスの生成に失敗しました.", ex);
        }

        permissionFlag = PluginPermission.parse(jsObject);

        return new Plugin(name, author, siteAddress, version, permissionFlag);
    }

    /**
     * プラグインの名前を取得します。
     * プラグイン名は必ず 1 文字以上の、半角英数、またはアンダーラインで構成されます。
     *
     * @return プラグイン名。
     */
    public String getName() {
        return this.name;
    }

    /**
     * プラグインの作者名を取得します。
     *
     * @return プラグインの作者名。
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * プラグインのサイトを表す URL を取得します。
     *
     * @return プラグインのサイトを表す URL オブジェクト。
     */
    public URL getSiteAddress() {
        return this.siteAddress;
    }

    /**
     * プラグインのバージョンを取得します。
     *
     * @return プラグインのバージョンを表す Version オブジェクト。
     */
    public Version getVersion() {
        return this.version;
    }

    /**
     * プラグインが要求するパーミッションを取得します。
     *
     * @return パーミッションを表す {@code EnumSet<PluginPermission>} オブジェクト。
     */
    public EnumSet<PluginPermission> getPermission() {
        return this.permission;
    }

    private static boolean checkNameString(String name) {
        return namePattern.matcher(name).find();
    }
}
