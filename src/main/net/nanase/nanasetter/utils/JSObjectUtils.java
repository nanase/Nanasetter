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

/**
 * Project: Nanasetter
 * Created by nanase on 14/06/22.
 */

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * JSObject の機能を拡張します。
 *
 * @author Tomona Nanase
 * @since Nanasetter 0.1
 */
public class JSObjectUtils {
    /**
     * 指定された JSObject に指定した名前を持つメンバが存在するかの真偽値を取得します。
     *
     * @param object 対象となる JSObject。
     * @param name   対象のメンバ名。
     * @return メンバが存在するとき true、それ以外のとき false。
     */
    public static boolean hasMember(JSObject object, String name) {
        if (object == null)
            return false;

        if (name == null)
            return false;

        try {
            return (boolean) object.eval("typeof this." + name + " !== 'undefined'");
        } catch (JSException e) {
            return false;
        }
    }

    /**
     * 指定された JSObject のメンバの型を文字列として取得します。
     *
     * @param object 対象となる JSObject。
     * @param name   対象のメンバ名。
     * @return JavaScriptでの型名。メンバが存在しない、何らかの理由で取得に失敗した場合は undefined。
     */
    public static String getTypeString(JSObject object, String name) {
        if (object == null)
            return "undefined";

        if (name == null)
            return "undefined";

        try {
            if (hasMember(object, name))
                return (String) object.eval("typeof this." + name);
        } catch (JSException e) {
            //
        }

        return "undefined";
    }

    /**
     * 指定された JSObject のメンバの一覧を取得します。
     *
     * @param object 対象となる JSObject。
     * @return メンバの一覧を内包した {@code Stream<String>}。
     */
    public static Stream<String> getMembersList(JSObject object) {
        if (object == null)
            return Stream.empty();

        try {
            return Stream.of(((String) object.eval("Object.keys(this).toString()")).split(","));
        } catch (JSException e) {
            //
        }

        return Stream.empty();
    }

    /**
     * 指定された JSObject に該当する型を持つメンバが存在するときに、指定された関数を実行します。
     *
     * @param object   対象となる JSObject。
     * @param name     対象のメンバ名。
     * @param tClass   メンバの {@code Class<T>} クラス。
     * @param consumer メンバが存在するときに実行される関数インタフェース。
     * @param <T>      メンバの型。
     */
    public static <T> void ifExists(JSObject object, String name, Class<T> tClass, Consumer<T> consumer) {
        if (object == null)
            return;

        if (name == null)
            return;

        if (consumer == null)
            return;

        ifExists(object, name, tClass, consumer, null);
    }

    /**
     * 指定された JSObject に該当する型を持つメンバの存在によって、指定された関数を実行します。
     *
     * @param object   対象となる JSObject。
     * @param name     対象のメンバ名。
     * @param tClass   メンバの {@code Class<T>} クラス。
     * @param consumer メンバが存在するときに実行される関数インタフェース。
     * @param elseRun  メンバが存在しない時に実行される関数インタフェース。
     * @param <T>      メンバの型。
     */
    public static <T> void ifExists(
            JSObject object,
            String name,
            Class<T> tClass,
            Consumer<T> consumer,
            Runnable elseRun) {

        if (object == null)
            return;

        if (name == null)
            return;

        Optional<T> member = getMember(object, name, tClass);

        if (member.isPresent() && consumer != null)
            consumer.accept(member.get());
        else if (elseRun != null)
            elseRun.run();
    }

    /**
     * 指定された JSObject に該当する型を持つメンバを加工し、取得します。
     *
     * @param object   対象となる JSObject。
     * @param name     対象のメンバ名。
     * @param tClass   メンバの {@code Class<T>} クラス。
     * @param function {@code <T>} 型のオブジェクトを加工し、{@code Optional<R>} 型に変換する
     *                 {@code Function<T, Optional<R>>} インタフェース。
     * @param <T>      メンバの型。
     * @param <R>      取得される型。
     * @return 加工されたメンバを内包する {@code Optional<R>} オブジェクト。
     */
    public static <T, R> Optional<R> process(
            JSObject object,
            String name,
            Class<T> tClass,
            Function<T, Optional<R>> function) {

        if (object == null)
            return Optional.empty();

        if (name == null)
            return Optional.empty();

        if (tClass == null)
            return Optional.empty();

        return process(object, name, tClass, function, null);
    }

    /**
     * 指定された JSObject に該当する型を持つメンバを加工し、取得します。
     *
     * @param object   対象となる JSObject。
     * @param name     対象のメンバ名。
     * @param tClass   メンバの {@code Class<T>} クラス。
     * @param function {@code <T>} 型のオブジェクトを加工し、{@code Optional<R>} 型に変換する
     *                 {@code Function<T, Optional<R>>} インタフェース。
     * @param orElse   メンバが存在しなかったときに実行される {@code Supplier<Optional<R>>} インタフェース。
     *                 {@code object}、{@code name} および {@code tClass} が null である場合はこのインタフェースは実行されません。
     * @param <T>      メンバの型。
     * @param <R>      取得される型。
     * @return 加工されたメンバを内包する {@code Optional<R>} オブジェクト。
     */
    public static <T, R> Optional<R> process(
            JSObject object,
            String name,
            Class<T> tClass,
            Function<T, Optional<R>> function,
            Supplier<Optional<R>> orElse) {

        if (object == null)
            return Optional.empty();

        if (name == null)
            return Optional.empty();

        if (tClass == null)
            return Optional.empty();

        Optional<T> member = getMember(object, name, tClass);

        if (member.isPresent() && function != null)
            return function.apply(member.get());
        else if (orElse != null)
            return orElse.get();
        else
            return Optional.empty();
    }

    /**
     * 指定された JSObject が配列であるかの真偽値を取得します。
     *
     * @param object 対象となる JSObject。
     * @return 配列であるとき true、それ以外のとき false。
     */
    public static boolean isArray(JSObject object) {
        return object != null && hasMember(object, "length");
    }

    /**
     * 指定された JSObject のメンバが配列であるかの真偽値を取得します。
     *
     * @param object 対象となる JSObject。
     * @param name   対象のメンバ名。
     * @return 配列であるとき true、それ以外のとき false。
     */
    public static boolean isArray(JSObject object, String name) {
        return object != null && name != null && hasMember(object, name + ".length");
    }

    /**
     * 指定された JSObject のメンバの値を取得します。
     *
     * @param object 対象となる JSObject。
     * @param name   対象のメンバ名。
     * @param tClass メンバの {@code Class<T>} クラス。
     * @param <T>    メンバの型。
     * @return メンバの値を内包した {@code Optional<T>}。
     */
    public static <T> Optional<T> getMember(JSObject object, String name, Class<T> tClass) {
        if (tClass == null)
            return Optional.empty();

        if (!hasMember(object, name))
            return Optional.empty();

        Object obj = object.getMember(name);

        if (tClass.isInstance(obj))
            return Optional.of(tClass.cast(obj));

        return Optional.empty();
    }

    /**
     * 指定された JSObject を配列として複数の値を取得します。
     *
     * @param object 対象となる JSObject。
     * @param tClass 配列の {@code Class<T>} クラス。
     * @param <T>    配列の型。
     * @return 配列を内包した {@code Stream<T>}。
     */
    public static <T> Stream<T> getArray(JSObject object, Class<T> tClass) {
        if (object == null)
            return Stream.empty();

        if (tClass == null)
            return Stream.empty();

        if (!isArray(object))
            return Stream.empty();

        int length = getMember(object, "length", Integer.class).get();
        List<T> list = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            Object obj = object.getSlot(i);

            if (tClass.isInstance(obj))
                list.add(tClass.cast(obj));
            else
                return Stream.empty();
        }

        return list.stream();
    }

    /**
     * 指定された JSObject のメンバを配列として複数の値を取得します。
     *
     * @param object 対象となる JSObject。
     * @param name   対象のメンバ名。
     * @param tClass 配列の {@code Class<T>} クラス。
     * @param <T>    配列の型。
     * @return 配列を内包した {@code Stream<T>}。
     */
    public static <T> Stream<T> getArray(JSObject object, String name, Class<T> tClass) {
        if (object == null)
            return Stream.empty();

        if (tClass == null)
            return Stream.empty();

        if (!isArray(object, name))
            return Stream.empty();

        Optional<JSObject> array = JSObjectUtils.getMember(object, name, JSObject.class);

        if (!array.isPresent())
            return Stream.empty();

        return getArray(array.get(), tClass);
    }
}
