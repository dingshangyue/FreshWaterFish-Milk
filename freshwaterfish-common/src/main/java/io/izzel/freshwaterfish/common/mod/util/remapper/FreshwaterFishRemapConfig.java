package io.izzel.freshwaterfish.common.mod.util.remapper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/*
 * Used to record transformation detail for specific ClassLoaders.
 */
public record FreshwaterFishRemapConfig(boolean remap) {
    public static final FreshwaterFishRemapConfig PLUGIN = new FreshwaterFishRemapConfig(true);

    public static FreshwaterFishRemapConfig read(DataInput input) throws IOException {
        return new FreshwaterFishRemapConfig(input.readBoolean());
    }

    public FreshwaterFishRemapConfig copy() {
        return new FreshwaterFishRemapConfig(remap);
    }

    public int write(DataOutput output) throws IOException {
        output.writeBoolean(remap);
        return 1;
    }
}
