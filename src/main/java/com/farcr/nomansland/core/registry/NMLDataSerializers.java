package com.farcr.nomansland.core.registry;

import com.farcr.nomansland.core.NoMansLand;
import com.farcr.nomansland.core.content.entity.variant.*;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NMLDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, NoMansLand.MODID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<GoatVariant>>> GOAT_VARIANT = ENTITY_DATA_SERIALIZERS.register("goat_variant", () -> EntityDataSerializer.forValueType(GoatVariant.STREAM_CODEC));
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<FoxVariant>>> FOX_VARIANT = ENTITY_DATA_SERIALIZERS.register("fox_variant", () -> EntityDataSerializer.forValueType(FoxVariant.STREAM_CODEC));
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<DolphinVariant>>> DOLPHIN_VARIANT = ENTITY_DATA_SERIALIZERS.register("dolphin_variant", () -> EntityDataSerializer.forValueType(DolphinVariant.STREAM_CODEC));
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<PigVariant>>> PIG_VARIANT = ENTITY_DATA_SERIALIZERS.register("pig_variant", () -> EntityDataSerializer.forValueType(PigVariant.STREAM_CODEC));
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<SalmonVariant>>> SALMON_VARIANT = ENTITY_DATA_SERIALIZERS.register("salmon_variant", () -> EntityDataSerializer.forValueType(SalmonVariant.STREAM_CODEC));
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<TurtleVariant>>> TURTLE_VARIANT = ENTITY_DATA_SERIALIZERS.register("turtle_variant", () -> EntityDataSerializer.forValueType(TurtleVariant.STREAM_CODEC));
}
