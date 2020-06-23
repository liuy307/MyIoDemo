import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class MyUtf8Decoder implements MyDecoder{
    private final int DEFAULT_BUFFER_SIZE = 30;
    private int bufferSize;
    private byte[] bufferByte;

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
        while((batch = is.read(bufferByte)) != -1) {
            System.out.println("本次读取了" + batch + "个字节");
        }
        System.out.println("读取字节流完毕，初始化完成！");
    }

    private int[] byte2UnsignedInt(){
        int[] buffer = new int[this.bufferByte.length];
        for(int i=0; i<this.bufferByte.length; ++i) {
            buffer[i] = Byte.toUnsignedInt(bufferByte[i]);
        }
        return buffer;
    }

    public char[] doDecode() {
        int[] bufferInt = byte2UnsignedInt();
        int byteLen = bufferInt.length;
        int charLen = byteLen;
        char[] results = new char[charLen];

        int bufferIndex = 0;
        int resultIndex = 0;
        int byteToParsed = bufferInt[bufferIndex];
        char ch='0';
        while((byteToParsed = bufferInt[bufferIndex]) != 0) {
            if(byteToParsed > 0 && byteToParsed < 127) {
                ch = (char) byteToParsed;
            }
            //110 10
            else if(byteToParsed >= 128+64 && byteToParsed < 128+64+32) {
                int ch1 = 0b00011111 & byteToParsed;
                bufferIndex++;
                byteToParsed = bufferInt[bufferIndex];
                int ch2 = 0b00111111 & byteToParsed;
                ch = (char)((ch1 << 6) + ch2);
            }
            //1110 10 10
            else if(byteToParsed >= 128+64+32 && byteToParsed < 128+64+32+16) {
                int ch1 = 0b00001111 & byteToParsed;
                bufferIndex++;
                byteToParsed = bufferInt[bufferIndex];
                int ch2 = 0b00111111 & byteToParsed;
                bufferIndex++;
                byteToParsed = bufferInt[bufferIndex];
                int ch3 = 0b00111111 & byteToParsed;
                ch = (char)((ch1 << 12) + (ch2 << 6) + ch3);
            }
            else if(byteToParsed >= 128+64+32+16 && byteToParsed < 128+64+32+16+8){
                int ch1 = 0b00000111 & byteToParsed;
                bufferIndex++;
                byteToParsed = bufferInt[bufferIndex];
                int ch2 = 0b00111111 & byteToParsed;
                bufferIndex++;
                byteToParsed = bufferInt[bufferIndex];
                int ch3 = 0b00111111 & byteToParsed;
                bufferIndex++;
                byteToParsed = bufferInt[bufferIndex];
                int ch4 = 0b00111111 & byteToParsed;
                ch = (char)((ch1 << 18) + (ch2 << 12) + (ch3 << 6) + ch4);
            }
            bufferIndex++;
            results[resultIndex++] = ch;
        }
        return results;
    }
}
