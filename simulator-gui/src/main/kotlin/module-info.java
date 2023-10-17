module com.tuxdave.mic1_simulator_kt.simulatorgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.kordamp.bootstrapfx.core;

    opens com.tuxdave.mic1_simulator_kt.simulatorgui to javafx.fxml;
    exports com.tuxdave.mic1_simulator_kt.simulatorgui;
}