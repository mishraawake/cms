package com.sp.dao.jcr;

import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.helper.DefUtils;
import com.sp.helper.PermissionUtils;
import com.sp.model.IDefinition;
import com.sp.model.Permission;
import com.sp.model.PojoFactory;
import com.sp.service.StringSerialization;
import com.sp.utils.SpringInitializer;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;

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

    private static final String outputFolder = "output";
    private static final String defFile = outputFolder+"/def.json";
    private static final String permFile = outputFolder + "/perm.json";


    @Test
    public void testDefinitionSerialization() throws Exception {
        IDefinition definition = DefUtils.getDummyDefinition(pojoFactory);
        IOUtils.write(serialization.serialize(definition), new FileOutputStream(defFile));
    }


    @Test
    public void testDefinitionDeserialize() throws  Exception {
        String str = IOUtils.toString(new FileInputStream(defFile));
        JCRDefinition definition = serialization.deserialize(str, JCRDefinition.class);
    }

    @Test
    public void testPermissionSerialization() throws Exception{
        Permission permission = PermissionUtils.getDummyPermission();
        IOUtils.write(serialization.serialize(permission), new FileOutputStream(permFile));
    }


    @Test
    public void testPermissionDeserialize() throws Exception{
            String str = IOUtils.toString(new FileInputStream(permFile));
            Permission definition = serialization.deserialize(str, Permission.class);
            return;
    }
}
