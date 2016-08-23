package com.sp.dao.jcr;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.helper.DefUtils;
import com.sp.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by pankajmishra on 07/08/16.
 */
public class DefinitionDaoTest extends BaseDaoTest {



    @Test
    public void testCreateDummyDefinition() throws DatabaseException {
        IDefinition definition = DefUtils.getDummyDefinition(pojoFactory);
        definitionDao.create(definition);
        Serializable id = definition.get__id();
        IDefinition definitionDB = definitionDao.get(id);
        compareDefinition(definition, definitionDB);
    }

    @Test
    public void testCreateSpecificDefinition() throws DatabaseException {

        IDefinition definition = DefUtils.getPoll(pojoFactory);
        definitionDao.create(definition);
        Serializable id = definition.get__id();
        IDefinition definitionDB = definitionDao.get(id);
        compareDefinition(definition, definitionDB);
    }

    @Test
    public void testUpdateDefinitionPollToArticle() throws DatabaseException {

        IDefinition definition = DefUtils.getPoll(pojoFactory);
        definitionDao.create(definition);
        Serializable id = definition.get__id();
        IDefinition definitionDB = definitionDao.get(id);
        compareDefinition(definition, definitionDB);
        IDefinition updatesDefinition = DefUtils.getArticle(pojoFactory);
        updatesDefinition.set__id(definitionDB.get__id());
        definitionDao.update(updatesDefinition);
        definitionDB = definitionDao.get(id);
        compareDefinition(updatesDefinition, definitionDB);
    }


    @Test
    public void testUpdateDefinitionArticleToPoll() throws DatabaseException {

        IDefinition definition = DefUtils.getArticle(pojoFactory);
        definitionDao.create(definition);
        Serializable id = definition.get__id();
        IDefinition definitionDB = definitionDao.get(id);
        compareDefinition(definition, definitionDB);
        IDefinition updatesDefinition = DefUtils.getPoll(pojoFactory);
        updatesDefinition.set__id(definitionDB.get__id());
        definitionDao.update(updatesDefinition);
        definitionDB = definitionDao.get(id);
        compareDefinition(updatesDefinition, definitionDB);
    }


    @Test
    public void testUpdateDefinitionPollToAnotherPoll() throws DatabaseException {

        IDefinition definition = DefUtils.getPoll(pojoFactory);
        definitionDao.create(definition);
        Serializable id = definition.get__id();
        IDefinition definitionDB = definitionDao.get(id);
        compareDefinition(definition, definitionDB);
        IDefinition updatesDefinition = DefUtils.getReversePoll(pojoFactory);
        updatesDefinition.set__id(definitionDB.get__id());
        definitionDao.update(updatesDefinition);
        definitionDB = definitionDao.get(id);
        compareDefinition(updatesDefinition, definitionDB);
    }


    @Test
    public void testDeleteAndExistsDefinition() throws DatabaseException {

        IDefinition definition = DefUtils.getDummyDefinition(pojoFactory);
        IDefinition definitionDB = definitionDao.create(definition);
        Assert.assertTrue(String.format("Created definition should be there in database with id %s ", definitionDB.get__id()
        ), definitionDao.exists(definitionDB.get__id()));
        definitionDao.delete(definition.get__id());
        Assert.assertFalse(String.format("Created definition should not be there in database with id %s ", definitionDB
                        .get__id()
        ), definitionDao.exists(definitionDB.get__id()) );
    }



    /**
     *
     * @param source
     * @param target
     */
    private void compareDefinition(IDefinition source, IDefinition target) {
        Assert.assertTrue(String.format("Definition name should match %s %s", source.getName(), target.getName()),
                source.getName().equals(target.getName())
        );
        Assert.assertTrue(String.format("Definition description should match %s %s", source.getDescription(),
                        target.getDescription()),
                source.getDescription().equals(target.getDescription())
        );
        Assert.assertTrue(String.format("Definition created date should match %s %s",
                        source.getCreateDate(), target.getCreatedBy()),
                source.getCreateDate().equals(target.getCreateDate())
        );

        for(Field sourceField : source.getFields()){

            Field targetField = target.getFieldByName(sourceField.getName());

            if(sourceField.getValueType().isDefinition()){
                DefinitionField field = (DefinitionField)sourceField;
                compareDefinition(field.getDefinition(),
                        ((DefinitionField)targetField).getDefinition() );
            } else {

                Assert.assertTrue(String.format("Definition field should match %s %s",
                                sourceField, targetField ),
                        sourceField.equals(targetField) );
            }
        }
    }


    @Override
    protected void specificBeforeSetup() throws DatabaseException {

    }

}
