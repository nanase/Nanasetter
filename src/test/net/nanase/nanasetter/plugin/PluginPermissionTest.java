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

import javafx.scene.web.WebEngine;
import net.nanase.nanasetter.JavaFXThreadingRule;
import netscape.javascript.JSObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.EnumSet;

import static net.nanase.nanasetter.plugin.PluginPermission.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PluginPermissionTest {

    @Rule
    public final JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private WebEngine webEngine;

    @Before
    public void setUp() throws Exception {
        this.webEngine = new WebEngine();
    }

    @Test
    public void testGetShortName() throws Exception {
        assertEquals("rest", READ_REST.getShortName());
        assertEquals("write", WRITE.getShortName());
        assertEquals("stream", READ_STREAMING.getShortName());
        assertEquals("extend", EXTEND.getShortName());
        assertEquals("config", CONFIGURE.getShortName());
        assertEquals("directMessage", ACCESS_DIRECT_MESSAGE.getShortName());
        assertEquals("risk", RISK.getShortName());
    }

    @Test
    public void testParseSet() throws Exception {
        JSObject jsObject = (JSObject) webEngine.executeScript(
                "([{ permission: 'full' }," +
                        "{ permission: 'none' }," +
                        "{ permission: 'foo' }])");

        assertEquals(FULL, parse((JSObject) jsObject.getSlot(0)));
        assertEquals(NONE, parse((JSObject) jsObject.getSlot(1)));

        // illegal
        try {
            parse((JSObject) jsObject.getSlot(2));
            fail();
        } catch (IllegalArgumentException e) {
            //
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testParseSingle() throws Exception {
        JSObject jsObject_none = (JSObject) webEngine.executeScript("({ permission: [] })");

        JSObject jsObject_short = (JSObject) webEngine.executeScript(
                "([{ permission: ['rest'] }," +
                        "{ permission: ['write'] }," +
                        "{ permission: ['stream'] }," +
                        "{ permission: ['extend'] }," +
                        "{ permission: ['config'] }," +
                        "{ permission: ['directMessage'] }," +
                        "{ permission: ['risk'] }])");

        JSObject jsObject_long = (JSObject) webEngine.executeScript(
                "([{ permission: ['READ_REST'] }," +
                        "{ permission: ['WRITE'] }," +
                        "{ permission: ['READ_STREAMING'] }," +
                        "{ permission: ['EXTEND'] }," +
                        "{ permission: ['CONFIGURE'] }," +
                        "{ permission: ['ACCESS_DIRECT_MESSAGE'] }," +
                        "{ permission: ['RISK'] }])");

        assertEquals(EnumSet.noneOf(PluginPermission.class), parse(jsObject_none));

        assertEquals(EnumSet.of(READ_REST), parse((JSObject) jsObject_short.getSlot(0)));
        assertEquals(EnumSet.of(WRITE), parse((JSObject) jsObject_short.getSlot(1)));
        assertEquals(EnumSet.of(READ_STREAMING), parse((JSObject) jsObject_short.getSlot(2)));
        assertEquals(EnumSet.of(EXTEND), parse((JSObject) jsObject_short.getSlot(3)));
        assertEquals(EnumSet.of(CONFIGURE), parse((JSObject) jsObject_short.getSlot(4)));
        assertEquals(EnumSet.of(ACCESS_DIRECT_MESSAGE), parse((JSObject) jsObject_short.getSlot(5)));
        assertEquals(EnumSet.of(RISK), parse((JSObject) jsObject_short.getSlot(6)));

        assertEquals(EnumSet.of(READ_REST), parse((JSObject) jsObject_long.getSlot(0)));
        assertEquals(EnumSet.of(WRITE), parse((JSObject) jsObject_long.getSlot(1)));
        assertEquals(EnumSet.of(READ_STREAMING), parse((JSObject) jsObject_long.getSlot(2)));
        assertEquals(EnumSet.of(EXTEND), parse((JSObject) jsObject_long.getSlot(3)));
        assertEquals(EnumSet.of(CONFIGURE), parse((JSObject) jsObject_long.getSlot(4)));
        assertEquals(EnumSet.of(ACCESS_DIRECT_MESSAGE), parse((JSObject) jsObject_long.getSlot(5)));
        assertEquals(EnumSet.of(RISK), parse((JSObject) jsObject_long.getSlot(6)));
    }

    @Test
    public void testParseMulti() throws Exception {
        JSObject jsObject_short = (JSObject) webEngine.executeScript(
                "([{ permission: ['rest', 'write'] }," +
                        "{ permission: ['write', 'stream'] }," +
                        "{ permission: ['stream', 'extend'] }," +
                        "{ permission: ['extend', 'config'] }," +
                        "{ permission: ['config', 'directMessage'] }," +
                        "{ permission: ['directMessage', 'risk'] }," +
                        "{ permission: ['risk', 'rest'] }])");

        JSObject jsObject_long = (JSObject) webEngine.executeScript(
                "([{ permission: ['READ_REST', 'WRITE'] }," +
                        "{ permission: ['WRITE', 'READ_STREAMING'] }," +
                        "{ permission: ['READ_STREAMING', 'EXTEND'] }," +
                        "{ permission: ['EXTEND', 'CONFIGURE'] }," +
                        "{ permission: ['CONFIGURE', 'ACCESS_DIRECT_MESSAGE'] }," +
                        "{ permission: ['ACCESS_DIRECT_MESSAGE', 'RISK'] }," +
                        "{ permission: ['RISK', 'READ_REST'] }])");

        assertEquals(EnumSet.of(READ_REST, WRITE), parse((JSObject) jsObject_short.getSlot(0)));
        assertEquals(EnumSet.of(WRITE, READ_STREAMING), parse((JSObject) jsObject_short.getSlot(1)));
        assertEquals(EnumSet.of(READ_STREAMING, EXTEND), parse((JSObject) jsObject_short.getSlot(2)));
        assertEquals(EnumSet.of(EXTEND, CONFIGURE), parse((JSObject) jsObject_short.getSlot(3)));
        assertEquals(EnumSet.of(CONFIGURE, ACCESS_DIRECT_MESSAGE), parse((JSObject) jsObject_short.getSlot(4)));
        assertEquals(EnumSet.of(ACCESS_DIRECT_MESSAGE, RISK), parse((JSObject) jsObject_short.getSlot(5)));
        assertEquals(EnumSet.of(RISK, READ_REST), parse((JSObject) jsObject_short.getSlot(6)));

        assertEquals(EnumSet.of(READ_REST, WRITE), parse((JSObject) jsObject_long.getSlot(0)));
        assertEquals(EnumSet.of(WRITE, READ_STREAMING), parse((JSObject) jsObject_long.getSlot(1)));
        assertEquals(EnumSet.of(READ_STREAMING, EXTEND), parse((JSObject) jsObject_long.getSlot(2)));
        assertEquals(EnumSet.of(EXTEND, CONFIGURE), parse((JSObject) jsObject_long.getSlot(3)));
        assertEquals(EnumSet.of(CONFIGURE, ACCESS_DIRECT_MESSAGE), parse((JSObject) jsObject_long.getSlot(4)));
        assertEquals(EnumSet.of(ACCESS_DIRECT_MESSAGE, RISK), parse((JSObject) jsObject_long.getSlot(5)));
        assertEquals(EnumSet.of(RISK, READ_REST), parse((JSObject) jsObject_long.getSlot(6)));
    }

    @Test
    public void testParseFailure() throws Exception {
        JSObject jsObject_wrongName = (JSObject) webEngine.executeScript(
                "([{ permission: ['read'] }, { permission: ['read', 'rest'] }])");

        try {
            parse(null);
            fail();
        } catch (IllegalArgumentException e) {
            //
        } catch (Exception e) {
            fail();
        }

        try {
            parse((JSObject) jsObject_wrongName.getSlot(0));
            fail();
        } catch (IllegalArgumentException e) {
            //
        } catch (Exception e) {
            fail();
        }

        try {
            parse((JSObject) jsObject_wrongName.getSlot(1));
            fail();
        } catch (IllegalArgumentException e) {
            //
        } catch (Exception e) {
            fail();
        }
    }
}