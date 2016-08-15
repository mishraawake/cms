package com.sp.dao.jcr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.dao.jcr.utils.JCRNodePropertyName;
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


    private String getPrefixedName(String name){
        return JcrDaoUtils.getPrefixedName(name);
    }



    private void copyDefToNode(JCRDefinition definition, Node node) throws RepositoryException, JsonProcessingException {

        node.setProperty(getPrefixedName(JCRNodePropertyName.NAME_LINK_NAME), definition.getName());
        node.setProperty(getPrefixedName(JCRNodePropertyName.DESC_LINK_NAME), definition.getDescription());
        Node fieldNode = node.addNode(JCRNodePropertyName.FIELDS_LINK_NAME);

        for(Field field : definition.getFields()){
            fieldNode.setProperty(getPrefixedName(field.getName()), serialization.serialize(field));
        }
    }

    private void copyNodeToDef(Node node, JCRDefinition definition) throws RepositoryException, IOException {
        definition.set__id(node.getPath());
        definition.setDescription(node.getProperty(getPrefixedName(JCRNodePropertyName.DESC_LINK_NAME)).getString());
        definition.setName(node.getProperty(getPrefixedName(JCRNodePropertyName.NAME_LINK_NAME)).getString());
        Node fieldNode = node.getNode(JCRNodePropertyName.FIELDS_LINK_NAME);
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
            Node definitionNode = null;
            String defJcrName = JcrDaoUtils.getPrefixedName(JCRNodePropertyName.DEF_LINK_NAME);
            if(session.getRootNode().hasNode(defJcrName)) {
                definitionNode = session.getRootNode().getNode(defJcrName);
            } else {
                definitionNode = session.getRootNode().addNode(defJcrName);
            }
            Node childNode = definitionNode.addNode(element.getIdentityForPath());
            element.set__id(childNode.getPath());
            copyDefToNode(element, childNode);
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
