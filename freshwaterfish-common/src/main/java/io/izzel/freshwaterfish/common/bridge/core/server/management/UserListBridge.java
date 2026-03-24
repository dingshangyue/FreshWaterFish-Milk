package io.izzel.freshwaterfish.common.bridge.core.server.management;

import java.util.Collection;

public interface UserListBridge<V> {

    Collection<V> bridge$getValues();
}
