package io.github.hiiragi283.material.api.material.property.component

import io.github.hiiragi283.material.api.material.ColorConvertible
import io.github.hiiragi283.material.api.material.FormulaConvertible
import io.github.hiiragi283.material.api.material.HTMaterial
import io.github.hiiragi283.material.api.material.MolarMassConvertible
import io.github.hiiragi283.material.api.material.property.HTMaterialProperty

@JvmDefaultWithCompatibility
interface HTComponentProperty<T : HTComponentProperty<T>> :
    HTMaterialProperty<T>,
    ColorConvertible,
    FormulaConvertible,
    MolarMassConvertible {
    override fun verify(material: HTMaterial) {
        material.properties.values.forEach {
            if (it.key != key && it is ColorConvertible) {
                throw IllegalStateException("Material: ${material.key} cannot have two or more properties implemented ColorConvertible!")
            }
            if (it.key != key && it is FormulaConvertible) {
                throw IllegalStateException("Material: ${material.key} cannot have two or more properties implemented FormulaConvertible!")
            }
            if (it.key != key && it is MolarMassConvertible) {
                throw IllegalStateException(
                    "Material: ${material.key} cannot have two or more properties implemented MolarMassConvertible!",
                )
            }
        }
    }
}
