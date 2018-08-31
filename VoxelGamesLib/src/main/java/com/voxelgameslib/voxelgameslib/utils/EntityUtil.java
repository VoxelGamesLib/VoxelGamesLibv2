package com.voxelgameslib.voxelgameslib.utils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import javax.annotation.Nonnull;

import org.bukkit.entity.EntityType;

public class EntityUtil {

    private static final Map<EntityType, Type> ENTITY_TYPE_TO_TYPE = ImmutableMap.<EntityType, Type>builder()
            .put(EntityType.ARROW, Type.PROJECTILE)
            .put(EntityType.BAT, Type.PASSIVE)
            .put(EntityType.BLAZE, Type.ANGRY)
            .put(EntityType.BOAT, Type.OTHER)
            .put(EntityType.CAVE_SPIDER, Type.PASSIVE)
            .put(EntityType.CHICKEN, Type.FRIENDLY)
            .put(EntityType.COMPLEX_PART, Type.OTHER)
            .put(EntityType.COW, Type.FRIENDLY)
            .put(EntityType.CREEPER, Type.FRIENDLY)
            .put(EntityType.DROPPED_ITEM, Type.OTHER)
            .put(EntityType.AREA_EFFECT_CLOUD, Type.UTILITY)
            .put(EntityType.ELDER_GUARDIAN, Type.ANGRY)
            .put(EntityType.WITHER_SKELETON, Type.ANGRY)
            .put(EntityType.STRAY, Type.ANGRY)
            .put(EntityType.EGG, Type.OTHER)
            .put(EntityType.ENDERMAN, Type.PASSIVE)
            .put(EntityType.ENDER_CRYSTAL, Type.OTHER)
            .put(EntityType.ENDER_DRAGON, Type.ANGRY)
            .put(EntityType.ENDER_PEARL, Type.OTHER)
            .put(EntityType.ENDER_SIGNAL, Type.OTHER)
            .put(EntityType.EXPERIENCE_ORB, Type.OTHER)
            .put(EntityType.FALLING_BLOCK, Type.OTHER)
            .put(EntityType.FIREBALL, Type.PROJECTILE)
            .put(EntityType.FIREWORK, Type.OTHER)
            .put(EntityType.FISHING_HOOK, Type.PROJECTILE)
            .put(EntityType.GHAST, Type.ANGRY)
            .put(EntityType.GIANT, Type.ANGRY)
            .put(EntityType.HORSE, Type.FRIENDLY)
            .put(EntityType.IRON_GOLEM, Type.UTILITY)
            .put(EntityType.ITEM_FRAME, Type.OTHER)
            .put(EntityType.LEASH_HITCH, Type.OTHER)
            .put(EntityType.LIGHTNING, Type.OTHER)
            .put(EntityType.MAGMA_CUBE, Type.ANGRY)
            .put(EntityType.MINECART, Type.CART)
            .put(EntityType.MINECART_CHEST, Type.CART)
            .put(EntityType.MINECART_COMMAND, Type.CART)
            .put(EntityType.MINECART_FURNACE, Type.CART)
            .put(EntityType.MINECART_HOPPER, Type.CART)
            .put(EntityType.MINECART_MOB_SPAWNER, Type.CART)
            .put(EntityType.MINECART_TNT, Type.CART)
            .put(EntityType.MUSHROOM_COW, Type.FRIENDLY)
            .put(EntityType.OCELOT, Type.FRIENDLY)
            .put(EntityType.PAINTING, Type.OTHER)
            .put(EntityType.PIG, Type.FRIENDLY)
            .put(EntityType.PIG_ZOMBIE, Type.PASSIVE)
            .put(EntityType.PLAYER, Type.OTHER)
            .put(EntityType.PRIMED_TNT, Type.OTHER)
            .put(EntityType.SHEEP, Type.FRIENDLY)
            .put(EntityType.SILVERFISH, Type.ANGRY)
            .put(EntityType.SKELETON, Type.ANGRY)
            .put(EntityType.SLIME, Type.ANGRY)
            .put(EntityType.SMALL_FIREBALL, Type.PROJECTILE)
            .put(EntityType.SNOWBALL, Type.PROJECTILE)
            .put(EntityType.SNOWMAN, Type.UTILITY)
            .put(EntityType.SPIDER, Type.PASSIVE)
            .put(EntityType.SPLASH_POTION, Type.OTHER)
            .put(EntityType.SQUID, Type.FRIENDLY)
            .put(EntityType.THROWN_EXP_BOTTLE, Type.OTHER)
            .put(EntityType.UNKNOWN, Type.OTHER)
            .put(EntityType.VILLAGER, Type.FRIENDLY)
            .put(EntityType.WEATHER, Type.OTHER)
            .put(EntityType.WITCH, Type.ANGRY)
            .put(EntityType.WITHER, Type.ANGRY)
            .put(EntityType.WITHER_SKULL, Type.PROJECTILE)
            .put(EntityType.WOLF, Type.PASSIVE)
            .put(EntityType.ZOMBIE, Type.ANGRY)
            .put(EntityType.HUSK, Type.ANGRY)
            .put(EntityType.SPECTRAL_ARROW, Type.PROJECTILE)
            .put(EntityType.SHULKER_BULLET, Type.PROJECTILE)
            .put(EntityType.DRAGON_FIREBALL, Type.PROJECTILE)
            .put(EntityType.ZOMBIE_VILLAGER, Type.ANGRY)
            .put(EntityType.SKELETON_HORSE, Type.ANGRY)
            .put(EntityType.ZOMBIE_HORSE, Type.ANGRY)
            .put(EntityType.ARMOR_STAND, Type.UTILITY)
            .put(EntityType.ENDERMITE, Type.ANGRY)
            .put(EntityType.GUARDIAN, Type.ANGRY)
            .put(EntityType.RABBIT, Type.FRIENDLY)
            .put(EntityType.DONKEY, Type.FRIENDLY)
            .put(EntityType.MULE, Type.FRIENDLY)
            .put(EntityType.EVOKER_FANGS, Type.PROJECTILE)
            .put(EntityType.EVOKER, Type.ANGRY)
            .put(EntityType.VEX, Type.ANGRY)
            .put(EntityType.VINDICATOR, Type.ANGRY)
            .put(EntityType.ILLUSIONER, Type.ANGRY)
            .put(EntityType.SHULKER, Type.ANGRY)
            .put(EntityType.POLAR_BEAR, Type.FRIENDLY)
            .put(EntityType.LLAMA, Type.FRIENDLY)
            .put(EntityType.LLAMA_SPIT, Type.PROJECTILE)
            .put(EntityType.PARROT, Type.FRIENDLY)
            .put(EntityType.LINGERING_POTION, Type.UTILITY)
            .put(EntityType.TIPPED_ARROW, Type.PROJECTILE)
            .put(EntityType.TURTLE, Type.PASSIVE)
            .put(EntityType.PHANTOM, Type.ANGRY)
            .put(EntityType.DOLPHIN, Type.PASSIVE)
            .put(EntityType.DROWNED, Type.ANGRY)
            .put(EntityType.COD, Type.PASSIVE)
            .put(EntityType.SALMON, Type.PASSIVE)
            .put(EntityType.PUFFERFISH, Type.PASSIVE)
            .put(EntityType.TROPICAL_FISH, Type.PASSIVE)
            .put(EntityType.TRIDENT, Type.PROJECTILE)
            .build();
    private static final Multimap<Type, EntityType> TYPE_TO_ENTITY_TYPES = HashMultimap.create();

    static {
        for (final EntityType type : EntityType.values()) {
            if (!ENTITY_TYPE_TO_TYPE.containsKey(type)) {
                throw new IllegalStateException("Entity type '" + type + "' has not been mapped");
            }
        }
        for (final Map.Entry<EntityType, Type> entry : ENTITY_TYPE_TO_TYPE.entrySet()) {
            TYPE_TO_ENTITY_TYPES.put(entry.getValue(), entry.getKey());
        }
    }

    public enum Type {
        PASSIVE,
        ANGRY,
        FRIENDLY,
        PROJECTILE,
        OTHER,
        UTILITY,
        CART
    }

    @Nonnull
    public static EntityType[] getAll(@Nonnull final Type... types) {
        return Arrays.stream(types)
                .map(TYPE_TO_ENTITY_TYPES::get)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Enum::name))
                .toArray(EntityType[]::new);
    }
}
