package com.farcr.nomansland.core.content.mixin.variants;

import com.farcr.nomansland.core.NoMansLand;
import com.farcr.nomansland.core.content.entity.variant.GoatVariant;
import com.farcr.nomansland.core.content.mixinduck.FoxDuck;
import com.farcr.nomansland.core.registry.NMLDataSerializers;
import com.farcr.nomansland.core.registry.NMLVariants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(Goat.class)
public abstract class GoatMixin extends MobMixin implements VariantHolder<Holder<GoatVariant>> {

    @Unique
    private static final EntityDataAccessor<Holder<GoatVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Goat.class, NMLDataSerializers.GOAT_VARIANT.get());
    @Unique
    private static final String VARIANT_KEY = "variant";
    @Unique
    private static final ResourceKey<GoatVariant> DEFAULT_VARIANT = ResourceKey.create(NMLVariants.GOAT_VARIANT_KEY, ResourceLocation.fromNamespaceAndPath(NoMansLand.MODID, "default"));

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_VARIANT_ID, this.registryAccess().registryOrThrow(NMLVariants.GOAT_VARIANT_KEY).getHolderOrThrow(DEFAULT_VARIANT));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.getVariant().unwrapKey().ifPresent((variant) -> {
            compound.putString(VARIANT_KEY, variant.location().toString());
        });
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        Optional.ofNullable(ResourceLocation.tryParse(compound.getString(VARIANT_KEY)))
                .map((string) -> ResourceKey.create(NMLVariants.GOAT_VARIANT_KEY, string))
                .flatMap((variant) -> this.registryAccess().registryOrThrow(NMLVariants.GOAT_VARIANT_KEY).getHolder(variant))
                .ifPresent(this::setVariant);
    }

    @Override
    protected void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        Registry<GoatVariant> registry = this.registryAccess().registryOrThrow(NMLVariants.GOAT_VARIANT_KEY);
        Stream<Holder.Reference<GoatVariant>> allVariants = registry.holders();
        List<Holder.Reference<GoatVariant>> possibleVariants = allVariants
                .filter((v) -> v.value().biomes().isPresent())
                .filter((v) -> v.value().biomes().get().contains(level.getBiome(this.blockPosition())))
                .toList();
        this.setVariant(possibleVariants.isEmpty() ? registry.getHolderOrThrow(DEFAULT_VARIANT) : possibleVariants.get(random.nextInt(possibleVariants.size())));
    }

    @Override
    public Holder<GoatVariant> getVariant() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    @Override
    public void setVariant(Holder<GoatVariant> variantHolder) {
        this.entityData.set(DATA_VARIANT_ID, variantHolder);
    }

    @Inject(method = "getBreedOffspring*", at = @At("RETURN"), cancellable = true)
    private void getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<AgeableMob> cir) {
        Goat goat = EntityType.GOAT.create(level);
        if (goat != null) {
            ((VariantHolder<Holder<GoatVariant>>)goat).setVariant(this.random.nextBoolean() ?  this.getVariant() : ((VariantHolder<Holder<GoatVariant>>)otherParent).getVariant());
        }
        cir.setReturnValue(goat);
    }
}
