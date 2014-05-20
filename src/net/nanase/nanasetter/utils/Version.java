package net.nanase.nanasetter.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nanase on 14/05/18.
 */
public class Version implements Comparable<Version> {
    private static final Pattern parsePattern;

    public final int major;
    public final int minor;
    public final int build;
    public final int revision;
    public final String suffix;

    static {
        parsePattern = Pattern.compile("^(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)(?:\\.(\\d+))?)?)?([^\\. ]\\S*)?$");
    }

    public Version(int major) {
        this(major, -1, -1, -1, null);
    }

    public Version(int major, int minor) {
        this(major, minor, -1, -1, null);
    }

    public Version(int major, int minor, int build) {
        this(major, minor, build, -1, null);
    }

    public Version(int major, int minor, int build, int revision) {
        this(major, minor, build, revision, null);
    }

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

    public static Version Parse(String text) {
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
