package com.voxelgameslib.voxelgameslib.utils;

import lombok.extern.java.Log;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Method;

@Log
public class NMSUtil {

    private static Method worldGetHandle;
    private static Class nmsWorldServer;
    private static Method worldServerFlushSave;

    public static void flushSaveQueue(World world) {
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


    private static Class getNmsClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getNmsVersion() + "." + name);
    }

    private static String getNmsVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
