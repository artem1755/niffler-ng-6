package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.tpl.Connections;
import guru.qa.niffler.jupiter.SuitExtension;

public class DatabasesExtension implements SuitExtension {
    @Override
    public void afterSuite() {
        Connections.closeAllConnections();
    }
}
