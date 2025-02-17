package io.github.hiiragi283.material.api.material

import io.github.hiiragi283.material.HTMaterials
import io.github.hiiragi283.material.api.material.flag.HTMaterialFlag
import io.github.hiiragi283.material.api.material.flag.HTMaterialFlagSet
import io.github.hiiragi283.material.api.material.property.HTMaterialProperty
import io.github.hiiragi283.material.api.material.property.HTMaterialPropertyMap
import io.github.hiiragi283.material.api.material.property.HTPropertyKey
import io.github.hiiragi283.material.api.shape.HTShapeKey
import io.github.hiiragi283.material.api.shape.HTShapes
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import java.awt.Color

class HTMaterial private constructor(
    val key: HTMaterialKey,
    val properties: HTMaterialPropertyMap,
    val flags: HTMaterialFlagSet,
    val color: Color,
    val formula: String,
    val molar: Double,
    val type: HTMaterialType,
) {
    //    Properties    //

    fun <T : HTMaterialProperty<T>> getProperty(key: HTPropertyKey<T>): T? = key.objClass.cast(properties[key])

    fun hasProperty(key: HTPropertyKey<*>): Boolean = key in properties

    fun getDefaultShape(): HTShapeKey? = when (type) {
        HTMaterialType.Gem.AMETHYST -> HTShapes.GEM
        HTMaterialType.Gem.COAL -> HTShapes.GEM
        HTMaterialType.Gem.CUBIC -> HTShapes.GEM
        HTMaterialType.Gem.DIAMOND -> HTShapes.GEM
        HTMaterialType.Gem.EMERALD -> HTShapes.GEM
        HTMaterialType.Gem.LAPIS -> HTShapes.GEM
        HTMaterialType.Gem.QUARTZ -> HTShapes.GEM
        HTMaterialType.Gem.RUBY -> HTShapes.GEM
        HTMaterialType.Metal -> HTShapes.INGOT
        HTMaterialType.Stone -> null
        HTMaterialType.Undefined -> null
        HTMaterialType.Wood -> null
    }

    //    Flags    //

    fun hasFlag(flag: HTMaterialFlag): Boolean = flag in flags

    //    Any    //

    override fun toString(): String = key.toString()

    companion object {
        //    Registry    //

        private val REGISTRY: MutableMap<HTMaterialKey, HTMaterial> = linkedMapOf()

        @JvmStatic
        fun getMaterialKeys(): Collection<HTMaterialKey> = REGISTRY.keys

        @JvmStatic
        fun getMaterials(): Collection<HTMaterial> = REGISTRY.values

        @JvmStatic
        fun getMaterial(key: HTMaterialKey): HTMaterial = REGISTRY[key] ?: throw IllegalStateException("Material: $key is not registered!")

        @JvmStatic
        fun getMaterialOrNull(key: HTMaterialKey): HTMaterial? = REGISTRY[key]

        @JvmStatic
        internal fun create(
            key: HTMaterialKey,
            properties: HTMaterialPropertyMap,
            flags: HTMaterialFlagSet,
            color: Color,
            formula: String,
            molar: Double,
            type: HTMaterialType,
        ): HTMaterial = HTMaterial(key, properties, flags, color, formula, molar, type).also {
            REGISTRY.putIfAbsent(key, it)
            HTMaterials.log("Material: $key registered!")
        }

        private val shapeKey = HTShapeKey("fluid")

        fun appendTooltip(
            material: HTMaterial,
            shapeKey: HTShapeKey?,
            stack: ItemStack,
            lines: MutableList<Text>,
        ) {
            // Title
            lines.add(TranslatableText("tooltip.ht_materials.material.title"))
            // Name
            val name: String = shapeKey?.getTranslatedName(material.key) ?: material.key.getTranslatedName()
            lines.add(TranslatableText("tooltip.ht_materials.material.name", name))
            // Type
            lines.add(TranslatableText("tooltip.ht_materials.material.type", material.type))
            // Formula
            material.formula.takeIf(String::isNotEmpty)?.let { formula: String ->
                lines.add(TranslatableText("tooltip.ht_materials.material.formula", formula))
            }
            // Molar Mass
            material.molar.takeIf { it > 0.0 }?.let { molar: Double ->
                lines.add(TranslatableText("tooltip.ht_materials.material.molar", molar))
            }
            // Tooltip from Properties
            material.properties.values.forEach { it.appendTooltip(material, shapeKey, stack, lines) }
        }
    }
}
