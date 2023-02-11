/*
 * Copyright (c) PORTB 2023
 *
 * Licensed under GNU LGPL v3
 * https://www.gnu.org/licenses/lgpl-3.0.txt
 */

package portb.biggerstacks.mixin.compat.backpacked;

import com.mrcrayfish.backpacked.network.message.MessageUpdateBackpack;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MessageUpdateBackpack.class)
public class MessageUpdateBackpackMixin
{
    //for some reason he uses writeVarInt and writeByte instead of writeItem to save the item to the buffer. Though readItem is used normally for decoding
    @Redirect(method = "writeBackpack",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/network/FriendlyByteBuf;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf writeIntInsteadOfByte(FriendlyByteBuf buf, int count)
    {
        buf.writeInt(count);
        return buf;
    }
}
