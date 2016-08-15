package com.sp.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by pankajmishra on 09/08/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringInitializer.class)
public class NameUtilsTest {

    @Test
    public void testSEOName(){
        String str = "my name";
        Assert.assertTrue(NameUtils.getJCRSEOLikeString(str).equals("my-name"));
        str = "my    name";
        Assert.assertTrue(NameUtils.getJCRSEOLikeString(str).equals("my-name"));
        str = "    my     name      ";
        Assert.assertTrue(NameUtils.getJCRSEOLikeString(str).equals("my-name"));
        str = "My name";
        Assert.assertTrue(NameUtils.getJCRSEOLikeString(str).equals("My-name"));
        str = "my name is pankaj     mishra             ";
        Assert.assertTrue(NameUtils.getJCRSEOLikeString(str).equals("my-name-is-pankaj-mishra"));
    }

}
