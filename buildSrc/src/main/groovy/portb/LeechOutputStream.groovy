package portb

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

class LeechOutputStream extends BufferedOutputStream {
    LeechOutputStream(OutputStream mirror, @ClosureParams(value = SimpleType, options = ["java.lang.String"]) Closure leecher) {
        super(new OutputStream() {
            @Override
            void write(int b) throws IOException {
                throw new RuntimeException("This method should never be called")
            }

            @Override
            void write(byte[] b, int off, int len) throws IOException {
                mirror.write(b, off, len)

                leecher(new String(b, off, len))
            }
        })
    }
}
