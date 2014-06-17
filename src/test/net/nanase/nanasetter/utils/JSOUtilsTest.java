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

import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSOUtilsTest {
    @Rule
    public final JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private static JSOUtils jsoUtils;

    class TestFlag {
        public boolean executed;
    }

    @Before
    public void setUp() throws Exception {
        if(jsoUtils == null) {
            final WebEngine webEngine = new WebEngine();

            Object obj = webEngine.executeScript("({ text: 'message', number: 42, boolean: true," +
                    "func: function() {}, object: { foo: 'bar' }, array: [1, 3, 5] })");

            assertTrue(obj instanceof JSObject);
            jsoUtils = new JSOUtils((JSObject) obj);
        }
    }

    @Test
    public void testIfExists() throws Exception {
        TestFlag flag = new TestFlag();
        Consumer<Object> function = c -> flag.executed = true;

        flag.executed = false;
        jsoUtils.ifExists("object", function);
        assertTrue(flag.executed);

        flag.executed = false;
        jsoUtils.ifExists("foo", function);
        assertFalse(flag.executed);

        flag.executed = false;
        jsoUtils.ifExists("text", function);
        assertTrue(flag.executed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfExistsWithNull() throws Exception {
        jsoUtils.ifExists(null, null);
    }

    @Test
    public void testIfExistsAsBoolean() throws Exception {
        TestFlag flag = new TestFlag();
        Consumer<Boolean> function = c -> flag.executed = true;

        flag.executed = false;
        jsoUtils.ifExistsAsBoolean("boolean", function);
        assertTrue(flag.executed);

        flag.executed = false;
        jsoUtils.ifExistsAsBoolean("foo", function);
        assertFalse(flag.executed);

        flag.executed = false;
        jsoUtils.ifExistsAsBoolean("text", function);
        assertFalse(flag.executed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfExistsAsBooleanWithNull() throws Exception {
        jsoUtils.ifExistsAsBoolean(null, null);
    }

    @Test
    public void testIfExistsAsString() throws Exception {
        TestFlag flag = new TestFlag();
        Consumer<String> function = c -> flag.executed = true;

        flag.executed = false;
        jsoUtils.ifExistsAsString("text", function);
        assertTrue(flag.executed);

        flag.executed = false;
        jsoUtils.ifExistsAsString("foo", function);
        assertFalse(flag.executed);

        flag.executed = false;
        jsoUtils.ifExistsAsString("object", function);
        assertFalse(flag.executed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfExistsAsStringWithNull() throws Exception {
        jsoUtils.ifExistsAsString(null, null);
    }

    @Test
    public void testIfExistsAsNumber() throws Exception {
        TestFlag flag = new TestFlag();
        Consumer<Number> function = c -> flag.executed = true;

        flag.executed = false;
        jsoUtils.ifExistsAsNumber("number", function);
        assertTrue(flag.executed);

        flag.executed = false;
        jsoUtils.ifExistsAsNumber("foo", function);
        assertFalse(flag.executed);

        flag.executed = false;
        jsoUtils.ifExistsAsNumber("object", function);
        assertFalse(flag.executed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfExistsAsNumberWithNull() throws Exception {
        jsoUtils.ifExistsAsNumber(null, null);
    }

    @Test
    public void testGetBoolean() throws Exception {
        assertFalse(jsoUtils.getBoolean("text").isPresent());
        assertFalse(jsoUtils.getBoolean("number").isPresent());
        assertTrue(jsoUtils.getBoolean("boolean").isPresent());
        assertFalse(jsoUtils.getBoolean("func").isPresent());
        assertFalse(jsoUtils.getBoolean("object").isPresent());
        assertFalse(jsoUtils.getBoolean("array").isPresent());
        assertFalse(jsoUtils.getBoolean("foo").isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBooleanWithNull() throws Exception {
        jsoUtils.getBoolean(null);
    }

    @Test
    public void testGetString() throws Exception {
        assertTrue(jsoUtils.getString("text").isPresent());
        assertFalse(jsoUtils.getString("number").isPresent());
        assertFalse(jsoUtils.getString("boolean").isPresent());
        assertFalse(jsoUtils.getString("func").isPresent());
        assertFalse(jsoUtils.getString("object").isPresent());
        assertFalse(jsoUtils.getString("array").isPresent());
        assertFalse(jsoUtils.getString("foo").isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStringWithNull() throws Exception {
        jsoUtils.getString(null);
    }

    @Test
    public void testGetNumber() throws Exception {
        assertFalse(jsoUtils.getNumber("text").isPresent());
        assertTrue(jsoUtils.getNumber("number").isPresent());
        assertFalse(jsoUtils.getNumber("boolean").isPresent());
        assertFalse(jsoUtils.getNumber("func").isPresent());
        assertFalse(jsoUtils.getNumber("object").isPresent());
        assertFalse(jsoUtils.getNumber("array").isPresent());
        assertFalse(jsoUtils.getNumber("foo").isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumberWithNull() throws Exception {
        jsoUtils.getNumber(null);
    }

    @Test
    public void testHasMember() throws Exception {
        assertTrue(jsoUtils.hasMember("text"));
        assertTrue(jsoUtils.hasMember("number"));
        assertTrue(jsoUtils.hasMember("boolean"));
        assertTrue(jsoUtils.hasMember("func"));
        assertTrue(jsoUtils.hasMember("object"));
        assertTrue(jsoUtils.hasMember("array"));
        assertFalse(jsoUtils.hasMember("foo"));
        assertFalse(jsoUtils.hasMember(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHasMemberWithNull() throws Exception {
        jsoUtils.hasMember(null);
    }

    @Test
    public void testGetTypeString() throws Exception {
        assertEquals("string", jsoUtils.getTypeString("text"));
        assertEquals("number", jsoUtils.getTypeString("number"));
        assertEquals("boolean", jsoUtils.getTypeString("boolean"));
        assertEquals("function", jsoUtils.getTypeString("func"));
        assertEquals("object", jsoUtils.getTypeString("object"));
        assertEquals("object", jsoUtils.getTypeString("array"));
    }

    @Test(expected = netscape.javascript.JSException.class)
    public void testGetTypeStringWithEmpty() throws Exception {
        jsoUtils.getTypeString("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeStringWithNull() throws Exception {
        jsoUtils.getTypeString(null);
    }
}