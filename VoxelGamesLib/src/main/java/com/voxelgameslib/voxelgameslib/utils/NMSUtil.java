package com.voxelgameslib.voxelgameslib.utils;

import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class NMSUtil {

    private static final Logger log = Logger.getLogger(NMSUtil.class.getName());
    private static Method worldGetHandle;
    private static Class nmsWorldServer;
    private static Method worldServerFlushSave;

    public static void flushSaveQueue(@Nonnull World world) {
        try {
            if (nmsWorldServer == null) {
                nmsWorldServer = getNmsClass("WorldServer");
            }
            if (worldGetHandle == null) {
                worldGetHandle = world.getClass().getMethod("getHandle");
            }
            if (worldServerFlushSave == null) {
                worldServerFlushSave = nmsWorldServer.getMethod("flushSave");
            }

            Object worldServer = worldGetHandle.invoke(world);
            worldServerFlushSave.invoke(worldServer);
        } catch (Exception ex) {
            log.warning("Error while trying to flush the save queue!");
            ex.printStackTrace();
        }
    }


    @Nonnull
    private static Class getNmsClass(@Nonnull String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getNmsVersion() + "." + name);
    }

    @Nonnull
    private static String getNmsVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
