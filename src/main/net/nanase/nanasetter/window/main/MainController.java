package net.nanase.nanasetter.window.main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.nanase.nanasetter.plugin.PluginLoader;
import net.nanase.nanasetter.twitter.TwitterList;
import net.nanase.nanasetter.window.dialog.Dialog;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainController implements Initializable {
    @FXML
    private WebView htmlRoot;

    @FXML
    private BorderPane root;

    private TwitterList twitterList;
    private Dialog dialog;
    private Logger logger;
    private PluginLoader pluginLoader;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.twitterList = new TwitterList();
        this.logger = Logger.getLogger("nanasetter");
    }

    public void setup() {
        this.dialog = new Dialog(this.root.getScene().getWindow());
        this.pluginLoader = new PluginLoader(this.dialog, this.logger);

        WebEngine webEngine = htmlRoot.getEngine();
        webEngine.setOnAlert(event -> this.dialog.info(event.getData()));
        webEngine.load(getClass().getResource("/page/index.html").toString());

        htmlRoot.setFontSmoothingType(FontSmoothingType.GRAY);
        htmlRoot.setContextMenuEnabled(false);
    }
}