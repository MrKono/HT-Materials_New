package io.github.hiiragi283.material.api.shape

import com.google.common.collect.ImmutableSet
import io.github.hiiragi283.material.HTMaterials
import io.github.hiiragi283.material.api.HTMaterialsAddon
import io.github.hiiragi283.material.api.util.addAll

object HTShapes : HTMaterialsAddon {
    //    Block    //

    @JvmField
    val BLOCK = HTShapeKey("block") // forgePath = "storage_blocks/%s"

    @JvmField
    val ORE = HTShapeKey("ore")

    //    Item    //

    @JvmField
    val DUST = HTShapeKey("dust")

    @JvmField
    val GEAR = HTShapeKey("gear")

    @JvmField
    val GEM = HTShapeKey("gem")

    @JvmField
    val INGOT = HTShapeKey("ingot")

    @JvmField
    val NUGGET = HTShapeKey("nugget")

    @JvmField
    val PLATE = HTShapeKey("plate")

    @JvmField
    val ROD = HTShapeKey("rod")

    //    Register    //

    override val modId: String = HTMaterials.MOD_ID

    override val priority: Int = -200

    override fun registerShape(registry: ImmutableSet.Builder<HTShapeKey>) {
        // Block
        registry.addAll(
            BLOCK,
            ORE,
        )
        // Item
        registry.addAll(
            DUST,
            GEAR,
            GEM,
            INGOT,
            NUGGET,
            PLATE,
            ROD,
        )
    }
}
