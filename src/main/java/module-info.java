module dev.philixtheexplorer.buggym {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires transitive javafx.graphics;

    requires org.commonmark;
    requires org.commonmark.ext.gfm.tables;

    requires org.fxmisc.richtext;
    requires reactfx;
    requires org.fxmisc.flowless;

    requires java.compiler;
    requires java.net.http;

    opens dev.philixtheexplorer.buggym to javafx.fxml;
    opens dev.philixtheexplorer.buggym.model to javafx.base;
    opens dev.philixtheexplorer.buggym.ui to javafx.fxml;

    exports dev.philixtheexplorer.buggym;
    exports dev.philixtheexplorer.buggym.model;
}
