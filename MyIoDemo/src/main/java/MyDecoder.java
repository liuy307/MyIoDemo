import java.io.IOException;

public interface MyDecoder {
    void init(String filePath) throws IOException;
    char[] doDecode();
}
