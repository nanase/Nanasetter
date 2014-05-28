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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: Nanasetter
 * Created by nanase on 14/05/18.
 */

/**
 * バージョンを表現し、比較するための機能を提供します。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class Version implements Comparable<Version> {
    private static final Pattern parsePattern;

    /** メジャーバージョンを表す数値。 */
    public final int major;

    /** マイナーバージョンを表す数値。 */
    public final int minor;

    /** ビルド番号を表す数値。 */
    public final int build;

    /** リビジョンを表す数値。 */
    public final int revision;

    /** サフィックス（接尾辞）を表す文字列。 */
    public final String suffix;

    static {
        parsePattern = Pattern.compile("^(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?)?([^\\. ]\\S*)?$");
    }

    /**
     * メジャーバージョンを使用してインスタンスを初期化します。
     *
     * @param major メジャーバージョンを表す数値。
     */
    public Version(int major) {
        this(major, -1, -1, -1, null);
    }

    /**
     * メジャーバージョンとマイナーバージョンを使用してインスタンスを初期化します。
     *
     * @param major メジャーバージョンを表す数値。
     * @param minor マイナーバージョンを表す数値。
     */
    public Version(int major, int minor) {
        this(major, minor, -1, -1, null);
    }

    /**
     * メジャーバージョン、マイナーバージョンおよびビルド番号を使用してインスタンスを初期化します。
     *
     * @param major メジャーバージョンを表す数値。
     * @param minor マイナーバージョンを表す数値。
     * @param build ビルド番号を表す数値。
     */
    public Version(int major, int minor, int build) {
        this(major, minor, build, -1, null);
    }

    /**
     * メジャーバージョン、マイナーバージョン、ビルド番号およびリビジョンを使用してインスタンスを初期化します。
     *
     * @param major メジャーバージョンを表す数値。
     * @param minor マイナーバージョンを表す数値。
     * @param build ビルド番号を表す数値。
     * @param revision リビジョンを表す数値。
     */
    public Version(int major, int minor, int build, int revision) {
        this(major, minor, build, revision, null);
    }

    /**
     * バージョン表現に用いられるすべてのパラメータを使用してインスタンスを初期化します。
     *
     * @param major メジャーバージョンを表す数値。
     * @param minor マイナーバージョンを表す数値。
     * @param build ビルド番号を表す数値。
     * @param revision リビジョンを表す数値。
     * @param suffix サフィックスを表す文字列。
     *               nullが与えられた場合は空の文字列として代用されます。
     */
    public Version(int major, int minor, int build, int revision, String suffix) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.revision = revision;

        this.suffix = (suffix == null) ? "" : suffix;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof Version))
            return false;

        Version other = (Version) obj;

        return (this.major == other.major) &&
                (this.minor == other.minor) &&
                (this.build == other.build) &&
                (this.revision == other.revision) &&
                this.suffix.equals(other.suffix);
    }

    @Override
    public String toString() {
        return String.format("%d%s%s%s%s",
                this.major,
                (this.minor > -1) ? "." + this.minor : "",
                (this.build > -1) ? "." + this.build : "",
                (this.revision > -1) ? "." + this.revision : "",
                this.suffix);
    }

    @Override
    public int compareTo(Version o) {
        if (o == null)
            throw new NullPointerException();

        if (this.major > o.major)
            return 1;
        else if (this.major < o.major)
            return -1;

        if (this.minor > o.minor)
            return 1;
        else if (this.minor < o.minor)
            return -1;

        if (this.build > o.build)
            return 1;
        else if (this.build < o.build)
            return -1;

        if (this.revision > o.revision)
            return 1;
        else if (this.revision < o.revision)
            return -1;

        // suffix での比較は行わない (行えない)
        return 0;
    }

    /**
     * 文字列で表現されたバージョンを解析し、新しい Version クラスのインスタンスを求めます。
     *
     * @param text 文字列で表現されたバージョン。
     *             各バージョン番号はピリオド '.' で分離され、サフィックスはバージョン部分に続いて記述されます。
     * @return 文字列を解析して得られた新しい Version クラスのインスタンス。
     */
    public static Version parse(String text) {
        if (text == null)
            throw new IllegalArgumentException("null");

        Matcher m = parsePattern.matcher(text);

        if (!m.find())
            throw new IllegalArgumentException(text);

        String major = m.group(1);
        String minor = m.group(2);
        String build = m.group(3);
        String revision = m.group(4);

        if (major == null || major.isEmpty())
            throw new IllegalArgumentException(text);

        return new Version(
                Integer.parseInt(major),
                (minor == null || minor.isEmpty()) ? -1 : Integer.parseInt(minor),
                (build == null || build.isEmpty()) ? -1 : Integer.parseInt(build),
                (revision == null || revision.isEmpty()) ? -1 : Integer.parseInt(revision),
                m.group(5));
    }
}
