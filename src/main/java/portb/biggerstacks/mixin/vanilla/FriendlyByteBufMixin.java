package portb.biggerstacks.mixin.vanilla;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * This modifies the way items counts are stored in a byte buffer. Vanilla stores item counts as a byte, which only allows values
 * up to 127. These mixins change it to be an int, which goes up to 2 billion (more than enough).
 * In code, the item count is treated as an int even though it is only supposed to go up to 64, so this will work fine.
 */
@Mixin(PacketBuffer.class)
public class FriendlyByteBufMixin
{
    /**
     * This writes the item count as an int instead of a byte.
     */
    @Redirect(method = "writeItemStack",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/network/PacketBuffer;writeByte(I)Lio/netty/buffer/ByteBuf;"))
    private ByteBuf writeBiggerStackCount(PacketBuffer instance, int count)
    {
        return instance.writeInt(count);
    }

    /**
     * We can't change the return type of the method, so instead we return a dummy value and modify the variable later.
     * This is essentially what this mixin and {@link #readStackItemCount} does: <br>
     * <pre>
     * {@code
     * //original code, reads item count as a byte
     * int count = buffer.readByte();
     * //the result of doNothing
     * int count = 0;
     * //the result of readStackItemCount, which reads the item count as an int instead of a byte
     * int count = readStackItemCount(0); //0 is unused
     * }
     * </pre>
     */
    @Redirect(method = "readItem",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;readByte()B"))
    private byte doNothing(PacketBuffer instance)
    {
        return 0; // do nothing, because we cannot change the return type of this method
    }

    /**
     * See {@link #doNothing(PacketBuffer)}
     */
    @ModifyVariable(method = "readItem", at = @At("STORE"), ordinal = 1)
    private int readStackItemCount(int value)
    {
        //actually read the count here
        return ((PacketBuffer) (Object) this).readInt();
    }
}
