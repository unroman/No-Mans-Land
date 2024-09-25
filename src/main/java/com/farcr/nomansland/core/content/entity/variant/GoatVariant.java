package com.farcr.nomansland.core.content.entity.variant;

import com.farcr.nomansland.core.registry.NMLVariants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Objects;

public record GoatVariant(ResourceLocation texture, HolderSet<Biome> biomes) {
    public static final Codec<GoatVariant> DIRECT_CODEC = RecordCodecBuilder.create((record) -> record.group(
                    ResourceLocation.CODEC.fieldOf("texture").forGetter((config) -> config.texture),
                    RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(GoatVariant::biomes))
            .apply(record, GoatVariant::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, GoatVariant> DIRECT_STREAM_CODEC;
    public static final Codec<Holder<GoatVariant>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<GoatVariant>> STREAM_CODEC;
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            boolean equals;
            if (other instanceof GoatVariant goatVariant) {
                equals = Objects.equals(this.texture, goatVariant.texture) && Objects.equals(this.biomes, goatVariant.biomes);
            } else {
                equals = false;
            }

            return equals;
        }
    }

    static {
        DIRECT_STREAM_CODEC = StreamCodec.composite(ResourceLocation.STREAM_CODEC, GoatVariant::texture, ByteBufCodecs.holderSet(Registries.BIOME), GoatVariant::biomes, GoatVariant::new);
        CODEC = RegistryFileCodec.create(NMLVariants.GOAT_VARIANT_KEY, DIRECT_CODEC);
        STREAM_CODEC = ByteBufCodecs.holder(NMLVariants.GOAT_VARIANT_KEY, DIRECT_STREAM_CODEC);
    }
}


