package in.yunyul.vertx.console.logging;

import in.yunyul.vertx.console.base.ConsolePage;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

@SuppressWarnings("unused")
public class LoggingConsolePage implements ConsolePage {
    @Override
    public void mount(Vertx vertx, Router router, String basePath) {
        new LoggingHandler(router, vertx, basePath);
    }

    @Override
    public String getLoaderFileName() {
        return "/js/logging.js";
    }
}
