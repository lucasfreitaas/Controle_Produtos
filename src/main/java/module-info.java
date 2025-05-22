module controle.produtos {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires static lombok;
    requires spring.security.crypto;
    requires java.desktop;
    requires itextpdf;

    exports org.principal;
    opens org.model to javafx.base, org.hibernate.orm.core, javafx.fxml;  // Modificado para incluir javafx.base
    opens org.controller to javafx.fxml;
    opens org.principal to javafx.fxml;
    opens org.controllerRelatorios to javafx.fxml;
}
