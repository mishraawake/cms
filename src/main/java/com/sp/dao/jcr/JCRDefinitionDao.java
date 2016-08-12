package com.sp.dao.jcr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.model.Field;
import com.sp.service.StringSerialization;
import com.sp.service.impl.JCRIdGenerator;
import com.sp.dao.jcr.utils.JCRRepository;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.jcr.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by pankajmishra on 07/08/16.
 */
@org.springframework.stereotype.Repository(value = "definitionDao")
public class JCRDefinitionDao implements DefinitionDao<JCRDefinition> {


    @Autowired
    StringSerialization serialization;

    @Autowired
    JCRIdGenerator idGenerator;


    public JCRDefinitionDao(){

    }

    private String getPrefixedName(String name){
        return JcrDaoUtils.getPrefixedName(name);
    }



    private void copyDefToNode(JCRDefinition definition, Node node) throws RepositoryException, JsonProcessingException {

        node.setProperty(getPrefixedName("name"), definition.getName());
        node.setProperty(getPrefixedName("description"), definition.getDescription());
        Node fieldNode = node.addNode("fields");

        for(Field field : definition.getFields()){
            fieldNode.setProperty(getPrefixedName(field.getName()), serialization.serialize(field));
        }
    }

    private void copyNodeToDef(Node node, JCRDefinition definition) throws RepositoryException, IOException {
        definition.set__id(node.getPath());
        definition.setDescription(node.getProperty(getPrefixedName("description")).getString());
        definition.setName(node.getProperty(getPrefixedName("name")).getString());
        Node fieldNode = node.getNode("fields");
        PropertyIterator propertyIterator = fieldNode.getProperties(JCRRepository.MY_NAME_SPACE_PREFIX + ":*");
        while(propertyIterator.hasNext()){
            Property  prop = propertyIterator.nextProperty();
            //System.out.println(prop.getName());
           definition.getFields().add(serialization.deserialize(prop.getString(), Field.class));
        }
    }

    @Override
    public JCRDefinition create(JCRDefinition element) throws DatabaseException {

        Session session = null;
        try {

            session = JCRRepository.getSession();
            if (element.getParentDefinition() != null) {
                Node parentNode = session.getNode(element.getParentDefinition().get__id());
                Node childNode = JcrDaoUtils.getCreatingChild(parentNode, idGenerator.getNextId(element));
                element.set__id(childNode.getPath());
                copyDefToNode(element, childNode);
            } else {
                // we need to create a domain
                Node childNode = JcrDaoUtils.getCreatingChild(session.getRootNode(), idGenerator.getNextId( element));
                element.set__id(childNode.getPath());
                copyDefToNode(element, childNode);
            }
            session.save();
            return element;

        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }


    @Override
    public JCRDefinition createOrUpdate(JCRDefinition element) throws DatabaseException {
        if(element.get__id() == null){
            return create(element);
        }
        return update(element);
    }

    @Override
    public JCRDefinition update(JCRDefinition element) throws DatabaseException {
        Session session = null;
        try {

            session = JCRRepository.getSession();
            copyDefToNode(element, session.getNode( element.get__id()));
            session.save();
            return element;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public JCRDefinition get(Serializable id) throws DatabaseException {
        Session session = null;
        try {
            JCRDefinition definition = new JCRDefinition();
            session = JCRRepository.getSession();


            copyNodeToDef(session.getNode((String)id) , definition);
            session.save();
            return definition;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public Iterable<JCRDefinition> list() {
        return null;
    }

    @Override
    public Long count() {
        return null;
    }

    @Override
    public void delete(Serializable id) throws DatabaseException {
        Session session = null;
        try {
            session = JCRRepository.getSession();
            session.removeItem((String) id);
            session.save();
        } catch (RepositoryException e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }

    @Override
    public boolean exists(Serializable id) throws DatabaseException {
        Session session = null;
        try {
            session = JCRRepository.getSession();
            return session.nodeExists((String)id);
        } catch (RepositoryException e) {
            throw new DatabaseException(e);
        } finally {
            if(session != null){
                session.logout();
            }
        }
    }
}
