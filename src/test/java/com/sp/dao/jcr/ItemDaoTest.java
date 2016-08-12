package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.dao.api.ItemDao;
import com.sp.model.*;
import com.sp.utils.DefUtils;
import com.sp.utils.ItemUtils;
import com.sp.utils.SpringInitializer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.io.Serializable;

/**
 * Created by pankajmishra on 08/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringInitializer.class)
public class ItemDaoTest implements ApplicationContextAware {

    ApplicationContext applicationContext;

    ItemDao<IItem> itemDao;

    DefinitionDao<IDefinition> definitionDao;

    @Autowired
    PojoFactory pojoFactory;

    @Test
    public void testCreation() throws DatabaseException {

        itemDao = applicationContext.getBean("itemDao", ItemDao.class);
        definitionDao = applicationContext.getBean("definitionDao", DefinitionDao.class);


        IDefinition definition = DefUtils.getDummyDefition(pojoFactory);
        definition = definitionDao.create(definition);
        IItem<IItem, IDefinition> item = ItemUtils.getDummyItem(pojoFactory);
        ;
        item.setDefinition(definition);
        item = itemDao.create(item);
        Serializable id = item.get__id();
        IItem<IItem, IDefinition> dataBaseItem = pojoFactory.getNewItem();
        dataBaseItem = itemDao.get(id);


        for (FieldValue fieldValue : dataBaseItem.getFieldValues()) {
            if ( fieldValue.getField().getValueType().equals(ValueType.ArrayOfImage)   ||    fieldValue.getField().getValueType().equals(ValueType.Image) ) {
                //System.out.println();
                try {

                    if(fieldValue.getField().getValueType().equals(ValueType.ArrayOfImage)) {
                        FileOutputStream fileOutputStream = new FileOutputStream("image.jpg");
                        fileOutputStream.write(((Image) fieldValue.getValue()).getBytes());
                        fileOutputStream.close();
                    }


                } catch (Exception e) {
                }
            } else if( fieldValue.getField().getValueType().equals(ValueType.ArrayOfVideo)   ||    fieldValue.getField().getValueType().equals(ValueType.Video) ) {
                //System.out.println();
                try {
                    if(fieldValue.getField().getValueType().equals(ValueType.ArrayOfVideo)) {
                        FileOutputStream fileOutputStream = new FileOutputStream("video.mp4");
                        fileOutputStream.write(((Video) fieldValue.getValue()).getBytes());
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                }
            } else if (  fieldValue.getField().getValueType().equals(ValueType.ArrayOfFile)   ||    fieldValue.getField().getValueType().equals(ValueType.File) ) {
                //System.out.println();
                try {
                    if(fieldValue.getField().getValueType().equals(ValueType.ArrayOfFile)) {
                        FileOutputStream fileOutputStream = new FileOutputStream("file.pdf");
                        fileOutputStream.write(((FileObject) fieldValue.getValue()).getBytes());
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                }
            } else {
                System.out.println(fieldValue.equals(item.getFieldValue(fieldValue.getField())));
                System.out.println(fieldValue);
                System.out.println(item.getFieldValue(fieldValue.getField()));
            }
        }
        System.out.println(item.get__id());

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
