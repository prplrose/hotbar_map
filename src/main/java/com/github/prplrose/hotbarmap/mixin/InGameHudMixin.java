package com.github.prplrose.hotbarmap.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Final @Shadow private MinecraftClient client;

    @Inject(method = "renderHotbarItem", at= @At(value = "HEAD"))
    void renderMap(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci){
        if (stack.isEmpty()) {
            return;
        }
        if(!stack.isOf(Items.FILLED_MAP)){
            return;
        }

        MapIdComponent mapIdComponent = stack.get(DataComponentTypes.MAP_ID);
        MapState mapState = FilledMapItem.getMapState(mapIdComponent, client.world);
        if (mapState != null){
            context.getMatrices().push();
            context.getMatrices().translate((float)(x), (float)(y), 200f);
            context.getMatrices().scale(1/8f,1/8f,1f);
            context.fill(0, 0, 128, 128, -1, 0xffd9c4a2);
            MinecraftClient.getInstance().gameRenderer.getMapRenderer().draw(context.getMatrices(), context.getVertexConsumers(), mapIdComponent, mapState, true, 255);
            context.getMatrices().pop();
        }

    }

}
