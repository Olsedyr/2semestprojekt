module CMS {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.web;

    opens CMS to javafx.fxml;
    exports CMS;
    exports CMS.Domain;
    opens CMS.Domain to javafx.fxml;
    exports CMS.Presentation;
    opens CMS.Presentation to javafx.fxml;

}