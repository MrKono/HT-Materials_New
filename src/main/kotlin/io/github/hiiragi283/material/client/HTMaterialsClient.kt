package io.github.hiiragi283.material.client

import io.github.hiiragi283.material.api.fluid.HTMaterialFluid
import io.github.hiiragi283.material.api.item.HTMaterialBlockItem
import io.github.hiiragi283.material.api.item.HTMaterialItem
import io.github.hiiragi283.material.api.material.HTMaterial
import io.github.hiiragi283.material.api.part.HTPartManager
import io.github.hiiragi283.material.common.HTMaterialsCommon
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.client.render.RenderLayer
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
object HTMaterialsClient : ClientModInitializer {

    override fun onInitializeClient() {

        //Register Default Model Consumer
        HTMaterialModelManager

        //Register Block Color Provider
        registerBlockColorProvider()
        HTMaterialsCommon.LOGGER.info("Block Color Provider Registered!")

        //Register Item Color Provider
        registerItemColorProvider()
        HTMaterialsCommon.LOGGER.info("Item Color Provider Registered!")

        //Register Render Handler for Material Fluid
        registerFluidRenderHandler()
        HTMaterialsCommon.LOGGER.info("Material Fluid Renderer Registered!")

        //Register Client Events
        registerEvents()
        HTMaterialsCommon.LOGGER.info("Client Events Registered!")

    }

    private fun registerBlockColorProvider() {
        //Material Blocks
        HTPartManager.getDefaultItemTable().values()
            .filterIsInstance<HTMaterialBlockItem>()
            .forEach { item: HTMaterialBlockItem ->
                ColorProviderRegistry.BLOCK.register(
                    BlockColorProvider { _, _, _, tintIndex: Int -> if (tintIndex == 0) item.materialHT.getColor() else -1 },
                    item.block
                )
                ColorProviderRegistry.ITEM.register(
                    ItemColorProvider { _, tintIndex: Int -> if (tintIndex == 0) item.materialHT.getColor() else -1 },
                    item
                )
            }
    }

    private fun registerItemColorProvider() {
        //Material Items
        HTPartManager.getDefaultItemTable().values()
            .filterIsInstance<HTMaterialItem>()
            .forEach { item: HTMaterialItem ->
                ColorProviderRegistry.ITEM.register(
                    ItemColorProvider { _, tintIndex: Int -> if (tintIndex == 0) item.materialHT.getColor() else -1 },
                    item
                )
            }
        //Material Fluid Bucket
        HTMaterialFluid.getBuckets().forEach { bucket: HTMaterialFluid.Bucket ->
            ColorProviderRegistry.ITEM.register(
                ItemColorProvider { _, tintIndex: Int -> if (tintIndex == 1) bucket.materialHT.getColor() else -1 },
                bucket
            )
        }
    }

    private fun registerFluidRenderHandler() {
        HTMaterial.REGISTRY.forEach { material: HTMaterial ->
            val flowing: HTMaterialFluid.Flowing = HTMaterialFluid.getFlowing(material) ?: return@forEach
            val still: HTMaterialFluid.Still = HTMaterialFluid.getStill(material) ?: return@forEach
            //Register Fluid Model
            FluidRenderHandlerRegistry.INSTANCE.register(
                still, flowing, SimpleFluidRenderHandler(
                    Identifier("minecraft:block/white_concrete"),
                    Identifier("minecraft:block/white_concrete"),
                    material.getColor()
                )
            )
            //Register Translucent Layer
            BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), still, flowing)
        }
    }

    private fun registerEvents() {

        ItemTooltipCallback.EVENT.register { stack: ItemStack, _, lines: MutableList<Text> ->
            HTPartManager.getPart(stack.item)?.appendTooltip(stack, lines)
        }

    }

}