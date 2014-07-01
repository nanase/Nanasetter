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

import javafx.scene.web.WebEngine;
import net.nanase.nanasetter.JavaFXThreadingRule;
import netscape.javascript.JSObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class JSObjectUtilsTest {
    @Rule
    public final JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private JSObject jsObject;

    @Before
    public void setUp() throws Exception {
        final WebEngine webEngine = new WebEngine();

        Object obj = webEngine.executeScript("({ text: 'message', number: 42, boolean: true," +
                "func: function() {}, object: { foo: 'bar' }, array: [1, 3, 5] })");

        assertTrue(obj instanceof JSObject);
        this.jsObject = (JSObject) obj;
    }

    @Test
    public void testHasMember() throws Exception {
        assertTrue(JSObjectUtils.hasMember(this.jsObject, "text"));
        assertTrue(JSObjectUtils.hasMember(this.jsObject, "number"));
        assertTrue(JSObjectUtils.hasMember(this.jsObject, "boolean"));
        assertTrue(JSObjectUtils.hasMember(this.jsObject, "func"));
        assertTrue(JSObjectUtils.hasMember(this.jsObject, "object"));
        assertTrue(JSObjectUtils.hasMember(this.jsObject, "array"));

        assertFalse(JSObjectUtils.hasMember(this.jsObject, "foo"));
        assertFalse(JSObjectUtils.hasMember(this.jsObject, " "));
        assertFalse(JSObjectUtils.hasMember(this.jsObject, ""));
        assertFalse(JSObjectUtils.hasMember(this.jsObject, null));

        assertFalse(JSObjectUtils.hasMember(null, null));
    }

    @Test
    public void testGetTypeString() throws Exception {
        assertEquals("string", JSObjectUtils.getTypeString(this.jsObject, "text"));
        assertEquals("number", JSObjectUtils.getTypeString(this.jsObject, "number"));
        assertEquals("boolean", JSObjectUtils.getTypeString(this.jsObject, "boolean"));
        assertEquals("function", JSObjectUtils.getTypeString(this.jsObject, "func"));
        assertEquals("object", JSObjectUtils.getTypeString(this.jsObject, "object"));
        assertEquals("object", JSObjectUtils.getTypeString(this.jsObject, "array"));

        assertEquals("undefined", JSObjectUtils.getTypeString(this.jsObject, "foo"));
        assertEquals("undefined", JSObjectUtils.getTypeString(this.jsObject, " "));
        assertEquals("undefined", JSObjectUtils.getTypeString(this.jsObject, ""));
        assertEquals("undefined", JSObjectUtils.getTypeString(this.jsObject, null));

        assertEquals("undefined", JSObjectUtils.getTypeString(null, null));
    }

    @Test
    public void testGetMembersList() throws Exception {
        assertArrayEquals(new String[]{"text", "number", "boolean", "func", "object", "array"},
                JSObjectUtils.getMembersList(this.jsObject).get());
    }

    @Test
    public void testIfExists() throws Exception {
        class Result {
            boolean r = false;
        }

        Result r = new Result();
        BiConsumer<String, Class<?>> truePattern = (s, c) -> {
            JSObjectUtils.ifExists(this.jsObject, s, c, k -> r.r = true);
            assertTrue(r.r);
            r.r = false;
        };
        BiConsumer<String, Class<?>> falsePattern = (s, c) -> {
            JSObjectUtils.ifExists(this.jsObject, s, c, k -> r.r = true);
            assertFalse(r.r);
            r.r = false;
        };
        BiConsumer<JSObject, Consumer<Object>> falsePattern2 = (s, c) -> {
            JSObjectUtils.ifExists(s, "text", Object.class, c);
            assertFalse(r.r);
            r.r = false;
        };

        truePattern.accept("text", String.class);
        truePattern.accept("number", Integer.class);
        truePattern.accept("boolean", Boolean.class);
        truePattern.accept("func", JSObject.class);
        truePattern.accept("object", JSObject.class);
        truePattern.accept("array", JSObject.class);

        falsePattern.accept("text", Integer.class);
        falsePattern.accept("number", Boolean.class);
        falsePattern.accept("boolean", JSObject.class);
        falsePattern.accept("func", Integer.class);
        falsePattern.accept("object", Integer.class);
        falsePattern.accept("array", Integer.class);

        falsePattern.accept("foo", Object.class);
        falsePattern.accept(" ", Object.class);
        falsePattern.accept("", Object.class);
        falsePattern.accept(null, Object.class);
        falsePattern.accept(null, null);

        falsePattern2.accept(this.jsObject, null);
        falsePattern2.accept(null, c -> r.r = true);
        falsePattern2.accept(null, null);
    }

    @Test
    public void testIfExists1() throws Exception {
        class Result {
            int r = 0;
        }

        Result r = new Result();
        BiConsumer<String, Class<?>> successPattern = (s, c) -> {
            JSObjectUtils.ifExists(this.jsObject, s, c, k -> r.r = 1, () -> r.r = -1);
            assertEquals(1, r.r);
            r.r = 0;
        };
        BiConsumer<String, Class<?>> failurePattern = (s, c) -> {
            JSObjectUtils.ifExists(this.jsObject, s, c, k -> r.r = 1, () -> r.r = -1);
            assertEquals(-1, r.r);
            r.r = 0;
        };
        BiConsumer<String, Class<?>> failurePattern2 = (s, c) -> {
            JSObjectUtils.ifExists(this.jsObject, s, c, k -> r.r = 1, () -> r.r = -1);
            assertEquals(0, r.r);
            r.r = 0;
        };

        successPattern.accept("text", String.class);
        successPattern.accept("number", Integer.class);
        successPattern.accept("boolean", Boolean.class);
        successPattern.accept("func", JSObject.class);
        successPattern.accept("object", JSObject.class);
        successPattern.accept("array", JSObject.class);

        failurePattern.accept("text", Integer.class);
        failurePattern.accept("number", Boolean.class);
        failurePattern.accept("boolean", JSObject.class);
        failurePattern.accept("func", Integer.class);
        failurePattern.accept("object", Integer.class);
        failurePattern.accept("array", Integer.class);

        failurePattern.accept("foo", Object.class);
        failurePattern.accept(" ", Object.class);
        failurePattern.accept("", Object.class);

        failurePattern2.accept(null, Object.class);
        failurePattern2.accept(null, null);
    }

    @Test
    public void testProcess() throws Exception {
        Optional<Integer> number = JSObjectUtils.process(this.jsObject, "number", Integer.class, Optional::of);
        assertTrue(number.isPresent());
        assertEquals(42, number.get().longValue());

        Optional<String> text = JSObjectUtils.process(this.jsObject, "text", String.class, Optional::of);
        assertTrue(text.isPresent());
        assertEquals("message", text.get());

        // null parameter
        assertFalse(JSObjectUtils.process(null, "number", Integer.class, Optional::of).isPresent());
        assertFalse(JSObjectUtils.process(this.jsObject, null, Integer.class, Optional::of).isPresent());
        assertFalse(JSObjectUtils.process(this.jsObject, "number", null, Optional::of).isPresent());
        assertFalse(JSObjectUtils.process(this.jsObject, "number", Integer.class, null).isPresent());

        // illegal value
        assertFalse(JSObjectUtils.process(this.jsObject, "text", Integer.class, Optional::of).isPresent());
        assertFalse(JSObjectUtils.process(this.jsObject, "number", String.class, Optional::of).isPresent());
    }

    @Test
    public void testProcess1() throws Exception {
        Optional<Integer> number;

        number = JSObjectUtils.process(this.jsObject, "number", Integer.class, Optional::of, () -> Optional.of(53));
        assertTrue(number.isPresent());
        assertEquals(42, number.get().longValue());

        // illegal value
        number = JSObjectUtils.process(this.jsObject, "text", Integer.class, Optional::of, () -> Optional.of(53));
        assertTrue(number.isPresent());
        assertEquals(53, number.get().longValue());

        // null parameter
        assertFalse(JSObjectUtils.process(null, "number", Integer.class, Optional::of, () -> Optional.of(53)).isPresent());
        assertFalse(JSObjectUtils.process(this.jsObject, null, Integer.class, Optional::of, () -> Optional.of(53)).isPresent());
        assertFalse(JSObjectUtils.process(this.jsObject, "number", null, Optional::of, () -> Optional.of(53)).isPresent());

        // `function' is null, but it will return 53.
        assertTrue(JSObjectUtils.process(this.jsObject, "number", Integer.class, null, () -> Optional.of(53)).isPresent());

        // same behavior process(JSObject, String, Class<T>, Function<T, R>).
        assertTrue(JSObjectUtils.process(this.jsObject, "number", Integer.class, Optional::of, null).isPresent());
    }

    @Test
    public void testIsArray() throws Exception {
        assertFalse(JSObjectUtils.isArray(this.jsObject));

        assertFalse(JSObjectUtils.isArray(null));
    }

    @Test
    public void testIsArray1() throws Exception {
        assertTrue(JSObjectUtils.isArray(this.jsObject, "text"));
        assertFalse(JSObjectUtils.isArray(this.jsObject, "number"));
        assertFalse(JSObjectUtils.isArray(this.jsObject, "boolean"));
        assertTrue(JSObjectUtils.isArray(this.jsObject, "func"));
        // assertFalse(JSObjectUtils.isArray(this.jsObject, "object"));
        assertTrue(JSObjectUtils.isArray(this.jsObject, "array"));

        assertFalse(JSObjectUtils.isArray(this.jsObject, "foo"));
        assertFalse(JSObjectUtils.isArray(this.jsObject, " "));
        assertFalse(JSObjectUtils.isArray(this.jsObject, ""));
        assertFalse(JSObjectUtils.isArray(this.jsObject, null));

        assertFalse(JSObjectUtils.isArray(null, null));
    }

    @Test
    public void testGetMember() throws Exception {
        assertTrue(JSObjectUtils.getMember(this.jsObject, "text", String.class).isPresent());
        assertTrue(JSObjectUtils.getMember(this.jsObject, "number", Integer.class).isPresent());
        assertTrue(JSObjectUtils.getMember(this.jsObject, "boolean", Boolean.class).isPresent());
        assertTrue(JSObjectUtils.getMember(this.jsObject, "func", JSObject.class).isPresent());
        assertTrue(JSObjectUtils.getMember(this.jsObject, "object", JSObject.class).isPresent());
        assertTrue(JSObjectUtils.getMember(this.jsObject, "array", JSObject.class).isPresent());

        assertFalse(JSObjectUtils.getMember(this.jsObject, "text", Double.class).isPresent());
        assertFalse(JSObjectUtils.getMember(this.jsObject, "number", Boolean.class).isPresent());
        assertFalse(JSObjectUtils.getMember(this.jsObject, "boolean", JSObject.class).isPresent());
        assertFalse(JSObjectUtils.getMember(this.jsObject, "func", Integer.class).isPresent());
        assertFalse(JSObjectUtils.getMember(this.jsObject, "object", Integer.class).isPresent());
        assertFalse(JSObjectUtils.getMember(this.jsObject, "array", Integer.class).isPresent());

        assertFalse(JSObjectUtils.getMember(null, "text", String.class).isPresent());
        assertFalse(JSObjectUtils.getMember(this.jsObject, null, String.class).isPresent());
        assertFalse(JSObjectUtils.getMember(this.jsObject, "text", null).isPresent());
    }

    @Test
    public void testGetArray() throws Exception {
        // prepare
        Optional<JSObject> array = JSObjectUtils.getMember(this.jsObject, "array", JSObject.class);
        assertTrue(array.isPresent());

        assertTrue(JSObjectUtils.getArray(array.get(), Integer.class).isPresent());

        // not array
        assertFalse(JSObjectUtils.getArray(this.jsObject, Object.class).isPresent());

        // null parameter
        assertFalse(JSObjectUtils.getArray(null, Integer.class).isPresent());
        assertFalse(JSObjectUtils.getArray(array.get(), null).isPresent());
        assertFalse(JSObjectUtils.getArray(null, null).isPresent());

        // type illegal
        assertFalse(JSObjectUtils.getArray(array.get(), String.class).isPresent());
        assertFalse(JSObjectUtils.getArray(array.get(), Double.class).isPresent());
    }

    @Test
    public void testGetArray1() throws Exception {
        assertTrue(JSObjectUtils.getArray(this.jsObject, "array", Integer.class).isPresent());

        // not array
        assertFalse(JSObjectUtils.getArray(this.jsObject, "text", String.class).isPresent());

        // null parameter
        assertFalse(JSObjectUtils.getArray(null, "array", Integer.class).isPresent());
        assertFalse(JSObjectUtils.getArray(this.jsObject, null, Integer.class).isPresent());
        assertFalse(JSObjectUtils.getArray(this.jsObject, "array", null).isPresent());
        assertFalse(JSObjectUtils.getArray(null, null, null).isPresent());

        // type illegal
        assertFalse(JSObjectUtils.getArray(this.jsObject, "array", Boolean.class).isPresent());
    }
}