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
import net.nanase.nanasetter.twitter.TwitterList;
import net.nanase.nanasetter.utils.JSObjectUtils;
import net.nanase.nanasetter.window.dialog.Dialog;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Project: Nanasetter
 * Created by nanase on 14/07/30.
 */

public class PluginLoader {
    private final List<PluginHost> pluginHosts;

    private final Logger logger;

    public PluginLoader(Dialog dialog, Logger logger) {
        this.pluginHosts = new ArrayList<>();
        this.logger = logger;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public List<PluginHost> getPluginHosts() {
        return this.pluginHosts;
    }

    public void loadPlugin(String directory, WebEngine webEngine, Dialog dialog, TwitterList twitterList) {
        Path path = Paths.get(directory);

        this.logger.info(String.format("ディレクトリ '%s' に対してプラグインを読み込みます.", directory));

        try (Stream<Path> stream = Files.list(path)) {
            stream.filter(Files::isDirectory)
                    .map(p -> p.resolve("plugin.js"))
                    .filter(Files::exists)
                    .forEach(f -> {
                        JSObject jsPlugin = loadPlugin(f, webEngine);

                        if (jsPlugin == null)
                            return;

                        if (!JSObjectUtils.hasMember(jsPlugin, "initialize")) {
                            this.logger.warning(String.format("ファイル '%s' が読み込まれましたが、必要なメソッドが定義されていません.", f.toString()));
                            return;
                        }

                        try {
                            JSObject info = JSObjectUtils.getMember(jsPlugin, "info", JSObject.class).orElse(null);
                            Plugin plugin = Plugin.create(info);

                            if (this.existsPluginByName(plugin.getName())) {
                                this.logger.warning(String.format("プラグイン '%s'(バージョン: %s) が読み込まれましたが、既に読み込まれています.",
                                        plugin.getName(), plugin.getVersion()));
                                return;
                            }

                            PluginHost host = new PluginHost(plugin, twitterList, dialog);

                            this.pluginHosts.add(host);
                            this.logger.info(String.format("プラグイン '%s' が読み込まれました(バージョン: %s).", plugin.getName(), plugin.getVersion()));

                            jsPlugin.call("initialize", new Object[]{host});
                        } catch (Exception ex) {
                            this.logger.warning(String.format("ファイル '%s' を初期化中にエラーが発生しました.", f.toString()));
                            this.logger.warning(ex.getMessage());
                        }
                    });
        } catch (IOException ex) {
            this.logger.warning(ex.getMessage());
        }

        this.logger.info("プラグインのロードが完了しました.");
    }

    private boolean existsPluginByName(String pluginName) {
        return this.pluginHosts.stream().anyMatch(p -> p.getPlugin().getName().equals(pluginName));
    }

    private JSObject loadPlugin(Path pluginFile, WebEngine webEngine) {
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("eval('");
            Files.readAllLines(pluginFile).forEach(s ->
                    sb.append(s.replace("'", "\\'").replace("\"", "\\\"")).append("\\n"));
            sb.append("')");

            String script = sb.toString();
            return (JSObject) webEngine.executeScript(script);
        } catch (IOException ex) {
            this.logger.warning(String.format("ファイル '%s' は読み込めません.", pluginFile.toString()));
            this.logger.warning(ex.getMessage());
            return null;
        } catch (JSException ex) {
            this.logger.warning(String.format("ファイル '%s' を初期化中にエラーが発生しました.", pluginFile.toString()));
            this.logger.warning(ex.getMessage());
            return null;
        } catch (Exception ex) {
            this.logger.warning(String.format("ファイル '%s' を読み込み中に不明なエラーが発生しました.", pluginFile.toString()));
            this.logger.warning(ex.getMessage());
            return null;
        }
    }
}
