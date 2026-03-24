package io.izzel.freshwaterfish.common.mod.permission;

import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.ServerOperator;

public class FreshwaterFishDummyPermissible extends PermissibleBase {

    public static final ServerOperator DUMMY_OPERATOR = new ServerOperator() {
        @Override
        public boolean isOp() {
            return false;
        }

        @Override
        public void setOp(boolean b) {
        }
    };

    public FreshwaterFishDummyPermissible() {
        super(DUMMY_OPERATOR);
    }
}