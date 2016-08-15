package com.sp.dao.jcr;

import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.model.IDefinition;
import com.sp.model.PojoFactory;
import com.sp.service.StringSerialization;
import com.sp.helper.DefUtils;
import com.sp.utils.SpringInitializer;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by pankajmishra on 07/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringInitializer.class)
public class TestSerialization {

    @Autowired
    StringSerialization serialization;

    @Autowired
    PojoFactory pojoFactory;

    @Test
    public void testSerialization(){
        IDefinition<IDefinition> definition = DefUtils.getDummyDefition(pojoFactory);
        try {
            IOUtils.write(serialization.serialize(definition) , new FileOutputStream("output/def.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDeserialize(){
        try {
            String str = IOUtils.toString(new FileInputStream("output/def.json"));
            JCRDefinition definition = serialization.deserialize(str, JCRDefinition.class);

          //  System.out.println(definition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
