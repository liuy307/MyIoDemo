import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MyUtf8DecoderTest {

    @Test
    public void test1() throws IOException {
        String filePath = MyUtf8Decoder.class.getClassLoader().getResource("test.txt").getFile();
        MyDecoder decoder = new MyUtf8Decoder(30);
        decoder.init(filePath);

        char[] results = decoder.doDecode();
        String str =new String(results);
        System.out.println(str);
        System.out.println(str.length());
    }
}