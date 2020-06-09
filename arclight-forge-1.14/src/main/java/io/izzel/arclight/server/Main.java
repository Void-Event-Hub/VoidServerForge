package io.izzel.arclight.server;

import io.izzel.arclight.api.EnumHelper;
import io.izzel.arclight.api.Unsafe;
import io.izzel.arclight.common.mod.util.log.ArclightI18nLogger;
import io.izzel.arclight.common.mod.util.log.ArclightLazyLogManager;
import io.izzel.arclight.common.mod.util.remapper.ArclightRemapper;
import io.izzel.arclight.forgeinstaller.ForgeInstaller;
import io.izzel.arclight.i18n.ArclightConfig;
import io.izzel.arclight.i18n.ArclightLocale;
import net.minecraftforge.server.ServerMain;

import java.util.Objects;

public class Main {

    public static void main(String[] args) throws Throwable {
        System.setProperty("java.util.logging.manager", ArclightLazyLogManager.class.getCanonicalName());
        System.setProperty("log4j.jul.LoggerAdapter", "io.izzel.arclight.common.mod.util.log.ArclightLoggerAdapter");
        ArclightLocale.info("i18n.using-language", ArclightConfig.spec().getLocale().getCurrent(), ArclightConfig.spec().getLocale().getFallback());
        ForgeInstaller.install();
        try { // Java 9 & Java 兼容性
            int javaVersion = (int) Float.parseFloat(System.getProperty("java.class.version"));
            if (javaVersion == 53) {
                throw new Exception("Only Java 8 and Java 10+ is supported.");
            }
            Unsafe.ensureClassInitialized(EnumHelper.class);
        } catch (Throwable t) {
            System.err.println("Your Java is not compatible with Arclight.");
            t.printStackTrace();
            return;
        }
        try {
            ArclightI18nLogger.getLogger("Arclight").info("loading-mapping");
            Objects.requireNonNull(ArclightRemapper.INSTANCE);
            ServerMain.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fail to launch Arclight.");
        }
    }
}
