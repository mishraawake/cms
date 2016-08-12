package com.sp.model;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class FileObject implements BinaryData {

    private InputStream is;

    private String mimeType;


    @Override
    public InputStream inputStream() {
        return is;
    }

    public void inputStream(InputStream stream){
        is = stream;
    }

    @Override
    public byte[] getBytes() {
        byte[] bytes = null;

        ByteOutputStream byteOutputStream = new ByteOutputStream();
        try {
            IOUtils.copy(is, byteOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = byteOutputStream.getBytes();
        return bytes;
    }


    @Override
    public String mimeType() {
        return mimeType;
    }

    public void mimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
