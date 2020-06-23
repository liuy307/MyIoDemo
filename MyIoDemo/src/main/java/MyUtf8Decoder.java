import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;

public class MyUtf8Decoder implements MyDecoder{
    private final int DEFAULT_BUFFER_SIZE = 100;
    private int bufferSize;
    private byte[] bufferByte;

    private int bytesLength;

    public MyUtf8Decoder() {
        this.bufferSize = DEFAULT_BUFFER_SIZE;
        this.bufferByte = new byte[this.bufferSize];
    }

    public MyUtf8Decoder(int initBufferSize) {
        this.bufferSize = initBufferSize;
        this.bufferByte = new byte[this.bufferSize];
    }

    public void init(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        int batch=0;
        while((batch = is.read(this.bufferByte)) != -1) {
            this.bytesLength = batch;
            System.out.println("本次读取了" + batch + "个字节");
        }
        System.out.println("读取字节流完毕，初始化完成！");
    }

    public char[] doDecode() {
        int bytesLength = this.bytesLength;

        char[] charsResult = new char[bytesLength];

        int bufferByteIndex = 0;
        int charsResultIndex = 0;
        byte currentByte = bufferByte[bufferByteIndex];
        int unsignedInt = Byte.toUnsignedInt(currentByte);
        char ch='0';
        while((currentByte = bufferByte[bufferByteIndex]) != 0) {
            unsignedInt = Byte.toUnsignedInt(bufferByte[bufferByteIndex]);

            if(unsignedInt > 0 && unsignedInt < 127) {
                ch = (char) currentByte;
            }
            //110 10
            else if(unsignedInt >= 128+64 && unsignedInt < 128+64+32) {
                int ch1 = 0b00011111 & currentByte;

                bufferByteIndex++;
                currentByte = bufferByte[bufferByteIndex];
                unsignedInt = Byte.toUnsignedInt(currentByte);
                int ch2 = 0b00111111 & unsignedInt;

                ch = (char)((ch1 << 6) + ch2);
            }
            //1110 10 10
            else if(unsignedInt >= 128+64+32 && unsignedInt < 128+64+32+16) {
                int ch1 = 0b00001111 & currentByte;

                bufferByteIndex++;
                currentByte = bufferByte[bufferByteIndex];
                unsignedInt = Byte.toUnsignedInt(currentByte);
                int ch2 = 0b00111111 & unsignedInt;

                bufferByteIndex++;
                currentByte = bufferByte[bufferByteIndex];
                unsignedInt = Byte.toUnsignedInt(currentByte);
                int ch3 = 0b00111111 & unsignedInt;

                ch = (char)((ch1 << 12) + (ch2 << 6) + ch3);
            }
            else if(currentByte >= 128+64+32+16 && currentByte < 128+64+32+16+8){
                int ch1 = 0b00000111 & currentByte;

                bufferByteIndex++;
                currentByte = bufferByte[bufferByteIndex];
                unsignedInt = Byte.toUnsignedInt(currentByte);
                int ch2 = 0b00111111 & unsignedInt;

                bufferByteIndex++;
                currentByte = bufferByte[bufferByteIndex];
                unsignedInt = Byte.toUnsignedInt(currentByte);
                int ch3 = 0b00111111 & unsignedInt;

                bufferByteIndex++;
                currentByte = bufferByte[bufferByteIndex];
                unsignedInt = Byte.toUnsignedInt(currentByte);
                int ch4 = 0b00111111 & unsignedInt;

                ch = (char)((ch1 << 18) + (ch2 << 12) + (ch3 << 6) + ch4);
            }
            bufferByteIndex++;
            charsResult[charsResultIndex++] = ch;
        }
        return Arrays.copyOf(charsResult, charsResultIndex);
    }
}
