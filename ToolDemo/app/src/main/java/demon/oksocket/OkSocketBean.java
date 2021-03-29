package demon.oksocket;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

/**
 * OkSocket通信：信息头 + 信息体
 */
public class OkSocketBean implements ISendable {

    private byte[] bytes;
    private byte[] newByte;

    public OkSocketBean(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] parse() {
        int length = bytes.length;
        newByte = new byte[length + 4];

        int count = length;
        for (int i = 3; i >= 0; i--) {
            newByte[i] = (byte) (count % 256);
            count = count / 256;
        }

        for (int i = 4; i < length + 4; i++) {
            newByte[i] = bytes[i - 4];
        }
        return newByte;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
