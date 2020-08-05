package club.sk1er.modelfix.asm

import club.sk1er.hookinjection.injectInstructions
import club.sk1er.modelfix.hook.ItemModelGeneratorHook
import club.sk1er.modelfix.tweaker.transformer.ModelFixTransformer
import org.objectweb.asm.tree.ClassNode

class ItemModelGeneratorTransformer : ModelFixTransformer {
    override fun getClassName() = arrayOf("net.minecraft.client.renderer.block.model.ItemModelGenerator")

    override fun transform(classNode: ClassNode, name: String) {
        classNode.methods.first {
            it.name == "func_178397_a"
        }?.apply {
            instructions.clear()
            injectInstructions {
                of(ItemModelGeneratorHook::generateModel)
                into(this@apply)
                params(1, 2, 3)
                keepReturns
            }
        }
    }
}