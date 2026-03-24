package io.izzel.freshwaterfish.common.bridge.core.entity.projectile;

import io.izzel.freshwaterfish.common.bridge.core.entity.EntityBridge;

public interface ArrowEntityBridge extends EntityBridge {

    void bridge$refreshEffects();

    boolean bridge$isTipped();
}
