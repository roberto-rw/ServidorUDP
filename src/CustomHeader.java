import java.nio.ByteBuffer;

public class CustomHeader {
    private int secuencia;

    public CustomHeader(int secuencia) {
        this.secuencia = secuencia;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(secuencia);
        return buffer.array();
    }

    public static CustomHeader fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int secuencia = buffer.getInt();
        return new CustomHeader(secuencia);
    }
}
