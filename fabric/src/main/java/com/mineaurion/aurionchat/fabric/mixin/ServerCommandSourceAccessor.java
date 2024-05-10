package com.mineaurion.aurionchat.fabric.mixin;

import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerCommandSource.class)
public interface ServerCommandSourceAccessor {

    @Accessor("output")
    CommandOutput getOutput();

}