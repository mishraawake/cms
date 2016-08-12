package com.sp.model;

import java.io.InputStream;

/**
 * Created by pankajmishra on 07/08/16.
 */
public interface BinaryData  {
    InputStream inputStream();
    void inputStream(InputStream is);
    byte[] getBytes();
    String mimeType();
    void mimeType(String mimeType);
}
