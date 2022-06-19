package portb.biggerstacks.mixin;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PacketBuffer.class)
public class FriendlyByteBufMixin
{
    @Redirect(method = "writeItemStack",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/network/PacketBuffer;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf writeBiggerStackCount(PacketBuffer instance, int count)
    {
        return instance.writeInt(count);
    }

    @Redirect(method = "readItem",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;readByte()B"))
    private byte doNothing(PacketBuffer instance)
    {
        return 0; // do nothing, because we cannot change the return type of this method
    }

    @ModifyVariable(method = "readItem", at = @At("STORE"), ordinal = 1)
    private int readStackItemCount(int value)
    {
        //actually read the count here
        return ((PacketBuffer) (Object) this).readInt();
    }
}
