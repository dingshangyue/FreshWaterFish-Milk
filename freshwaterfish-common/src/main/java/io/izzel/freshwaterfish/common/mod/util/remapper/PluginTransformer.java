package io.izzel.freshwaterfish.common.mod.util.remapper;

import org.objectweb.asm.tree.ClassNode;

public interface PluginTransformer {

    void handleClass(ClassNode node, ClassLoaderRemapper remapper, FreshwaterFishRemapConfig config);

    default int priority() {
        return 0;
    }
}
