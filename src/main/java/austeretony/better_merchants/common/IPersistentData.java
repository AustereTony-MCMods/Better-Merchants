package austeretony.better_merchants.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public interface IPersistentData {

    String getPath();

    void write(BufferedOutputStream bos) throws IOException;

    void read(BufferedInputStream bis) throws IOException;
}