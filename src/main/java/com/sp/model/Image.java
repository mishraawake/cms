package com.sp.model;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class Image implements BinaryData {

    private InputStream is;

    private String mimeType;


    @Override
    public InputStream inputStream() {
        return is;
    }

    public void inputStream(InputStream stream) {
        is = stream;
    }

    @Override
    public byte[] getBytes() {

        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String mimeType() {
        return mimeType;
    }

    public void mimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
