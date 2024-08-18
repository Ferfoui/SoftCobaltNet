package fr.ferfoui.softcobalt.client.gui;

import java.net.URL;

public class GuiResources {

    public static final URL COBALT_APPLICATION_FXML = getGuiResource("CobaltApplication.fxml");

    private static URL getGuiResource(String resource) {
        return GuiResources.class.getResource("/fr/ferfoui/softcobalt/client/gui/" + resource);
    }

}
