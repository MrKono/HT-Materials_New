package io.github.hiiragi283.material.api.material.property

import io.github.hiiragi283.material.api.material.HTMaterial
import io.github.hiiragi283.material.api.shape.HTShapeKey
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

@JvmDefaultWithCompatibility
interface HTMaterialProperty<T : HTMaterialProperty<T>> {
    val key: HTPropertyKey<T>

    fun verify(material: HTMaterial)

    fun appendTooltip(
        material: HTMaterial,
        shapeKey: HTShapeKey?,
        stack: ItemStack,
        lines: MutableList<Text>,
    ) {}
}
