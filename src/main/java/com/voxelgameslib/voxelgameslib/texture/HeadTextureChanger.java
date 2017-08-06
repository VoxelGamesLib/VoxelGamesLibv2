/*
 * Copyright 2016 inventivetalent. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package com.voxelgameslib.voxelgameslib.texture;

import com.google.common.io.BaseEncoding;

import org.inventivetalent.mcwrapper.auth.GameProfileWrapper;
import org.inventivetalent.mcwrapper.auth.properties.PropertyWrapper;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ClassResolver;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
import org.inventivetalent.reflection.resolver.minecraft.OBCClassResolver;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("MissingJSR305")
public class HeadTextureChanger {

    static final ClassResolver classResolver = new ClassResolver();
    static final NMSClassResolver nmsClassResolver = new NMSClassResolver();
    static final OBCClassResolver obcClassResolver = new OBCClassResolver();

    static Class<?> World = nmsClassResolver.resolveSilent("World");
    static final MethodResolver WorldMethodResolver = new MethodResolver(World);
    static Class<?> WorldServer = nmsClassResolver.resolveSilent("WorldServer");
    static final MethodResolver WorldServerMethodResolver = new MethodResolver(WorldServer);
    static Class<?> TileEntitySkull = nmsClassResolver.resolveSilent("TileEntitySkull");
    static final MethodResolver TileEntitySkullMethodResolver = new MethodResolver(TileEntitySkull);
    static final FieldResolver TileEntitySkullFieldResolver = new FieldResolver(TileEntitySkull);
    static Class<?> NBTTagCompound = nmsClassResolver.resolveSilent("NBTTagCompound");
    static final MethodResolver NBTTagCompoundMethodResolver = new MethodResolver(NBTTagCompound);
    static final ConstructorResolver NBTTagCompoundConstructorResolver = new ConstructorResolver(NBTTagCompound);
    static Class<?> NBTBase = nmsClassResolver.resolveSilent("NBTBase");
    static Class<?> GameProfileSerializer = nmsClassResolver.resolveSilent("GameProfileSerializer");
    static final MethodResolver GameProfileSerializerMethodResolver = new MethodResolver(GameProfileSerializer);
    static Class<?> CraftMetaSkull = obcClassResolver.resolveSilent("inventory.CraftMetaSkull");
    static final FieldResolver CraftMetaSkullFieldResolver = new FieldResolver(CraftMetaSkull);
    static final ConstructorResolver CraftMetaSkullConstructorResolver = new ConstructorResolver(CraftMetaSkull);
    static Class<?> GameProfile = classResolver.resolveSilent("net.minecraft.util.com.mojang.authlib.GameProfile", "com.mojang.authlib.GameProfile");

    public static String encodeBase64(byte[] bytes) {
        return BaseEncoding.base64().encode(bytes);
    }

    public static String buildResourceLocation(String url) {
        String format = "{textures:{SKIN:{url:\"%s\"}}}";
        return String.format(format, url);
    }

    public static Object createProfile(String data) {
        try {
            GameProfileWrapper profileWrapper = new GameProfileWrapper(UUID.randomUUID(), "CustomBlock");
            PropertyWrapper propertyWrapper = new PropertyWrapper("textures", data);
            profileWrapper.getProperties().put("textures", propertyWrapper);

            return profileWrapper.getHandle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createProfile(String value, String signature) {
        if (signature == null) {
            return createProfile(value);
        }
        try {
            GameProfileWrapper profileWrapper = new GameProfileWrapper(UUID.randomUUID(), "CustomBlock");
            PropertyWrapper propertyWrapper = new PropertyWrapper("textures", value, signature);
            profileWrapper.getProperties().put("textures", propertyWrapper);

            return profileWrapper.getHandle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void applyTextureToSkull(Skull skull, Object profile) throws Exception {
        Location location = skull.getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Object world = Minecraft.getHandle(location.getWorld());
        Object tileEntity = WorldServerMethodResolver.resolve("getTileEntity").invoke(world, x, y, z);
        TileEntitySkullFieldResolver.resolveByFirstType(GameProfile).set(tileEntity, profile);
        WorldMethodResolver.resolve(new ResolverQuery("notify", int.class, int.class, int.class)).invoke(world, x, y, z);
    }

    public static SkullMeta applyTextureToMeta(Object meta, Object profile) throws Exception {
        if (meta == null) {
            throw new IllegalArgumentException("meta cannot be null");
        }
        if (profile == null) {
            throw new IllegalArgumentException("profile cannot be null");
        }
        Object baseNBTTag = NBTTagCompound.newInstance();
        Object ownerNBTTag = NBTTagCompound.newInstance();

        GameProfileSerializerMethodResolver.resolve(new ResolverQuery("serialize", NBTTagCompound, GameProfile)).invoke(null, ownerNBTTag, profile);
        NBTTagCompoundMethodResolver.resolve(new ResolverQuery("set", String.class, NBTBase)).invoke(baseNBTTag, "SkullOwner", ownerNBTTag);

        SkullMeta newMeta = (SkullMeta) CraftMetaSkullConstructorResolver.resolve(new Class[]{NBTTagCompound}).newInstance(baseNBTTag);

        Field profileField = CraftMetaSkullFieldResolver.resolve("profile");
        profileField.set(meta, profile);
        profileField.set(newMeta, profile);

        return newMeta;
    }

}