package com.sp.dao.jcr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.dao.api.JCRIRepository;
import com.sp.dao.jcr.model.JCRDefinition;
import com.sp.dao.jcr.model.JcrNameFac;
import com.sp.dao.jcr.utils.FixedNames;
import com.sp.dao.jcr.utils.JcrDaoUtils;
import com.sp.model.DefinitionField;
import com.sp.model.Field;
import com.sp.service.StringSerialization;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pankajmishra on 07/08/16.
 */
@org.springframework.stereotype.Repository(value = "definitionDao")
public class JCRDefinitionDao implements DefinitionDao<JCRDefinition> {

    @Autowired
    StringSerialization serialization;

    @Autowired
    JCRIRepository jcriRepository;


    @Override
    public JCRDefinition create(JCRDefinition element) throws DatabaseException {

        Session session = null;
        try {

            session = jcriRepository.getSession();
            Node definitionNode = JcrDaoUtils.getNode(session.getRootNode(), FixedNames.defs());
            if(definitionNode == null ){
                definitionNode = JcrDaoUtils.addNode(session.getRootNode(), FixedNames.defs());
            }
            Node childNode = JcrDaoUtils.addNode(definitionNode, JcrNameFac.getUserName(element.getIdentityForPath()));
            element.set__id(childNode.getPath());
            copyDefToNode(element, childNode, session);
            session.save();
            return element;

        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }


    @Override
    public JCRDefinition createOrUpdate(JCRDefinition element) throws DatabaseException {
        if (element.get__id() == null) {
            return create(element);
        }
        return update(element);
    }

    @Override
    public JCRDefinition update(JCRDefinition element) throws DatabaseException {
        Session session = null;
        try {
            session = jcriRepository.getSession();
            updateDefToNode(element, session.getNode((String) element.get__id()), session);
            session.save();
            return element;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }


    private void updateDefToNodeNormalField(JCRDefinition updatedObj, Node existingNode, Session session) throws
            RepositoryException, IOException {
        // collect all none definition type fields
        List<String> keys = updatedObj.getFields().stream().filter(f -> !f.getValueType().isDefinition()).
                map(Field::getName).collect(Collectors.toList());
        List<String> updateKeys = new ArrayList<>();
        Node fieldNode = JcrDaoUtils.getNode(existingNode, FixedNames.fields());
        if(fieldNode != null){
            PropertyIterator propertyIterator = fieldNode.getProperties( JcrNameFac.getUserName("*").pattern());
            while (propertyIterator.hasNext()) {
                Property prop = propertyIterator.nextProperty();
                Field field = serialization.deserialize(prop.getString(), Field.class);
                Field updatedField = updatedObj.getFieldByName(field.getName());
                if(updatedField != null){
                    updateKeys.add(field.getName());
                    prop.setValue(serialization.serialize(updatedField));
                } else {
                    prop.remove();
                }
            }
        }

        // remaining properties to be updated are..
        keys.removeAll(updateKeys);
        if(!keys.isEmpty()){
            fieldNode = JcrDaoUtils.createIfNotExist(existingNode, FixedNames.fields());
            for(String key : keys){
                Value serJsonField = session.getValueFactory().createValue(serialization.serialize(updatedObj
                        .getFieldByName(key)));
                JcrDaoUtils.setProperty(fieldNode, JcrNameFac.getUserName(key), serJsonField);
            }
        }
    }


    private void updateDefToNodeDefinitionField(JCRDefinition updatedObj, Node existingNode, Session session) throws
            RepositoryException, IOException {

        List<String> defKeys = updatedObj.getFields().stream().filter(f -> f.getValueType().isDefinition()).
                map(Field::getName).collect(Collectors.toList());

        List<String>  updateDefKeys = new ArrayList<>();
        NodeIterator defNodeIterator = existingNode.getNodes(JcrNameFac.getUserName("*").pattern());
        while (defNodeIterator.hasNext()){
            Node defNode = defNodeIterator.nextNode();
            String jsonDefField = JcrDaoUtils.getProperty(defNode, FixedNames.field()).getString();
            DefinitionField definitionField = serialization.deserialize(jsonDefField, DefinitionField.class);
            DefinitionField updatedDefField =  (DefinitionField)updatedObj.getFieldByName(definitionField.getName());
            if(updatedDefField != null){
                JCRDefinition jcrDefinition = (JCRDefinition)updatedDefField.getDefinition();
                JcrDaoUtils.setProperty(defNode, FixedNames.field(), serialization.serialize(updatedDefField));
                updateDefToNode(jcrDefinition, defNode, session);
                updateDefKeys.add(definitionField.getName());
            } else {
                defNode.remove();
            }
        }

        // remaining properties to be updated are..
        defKeys.removeAll(updateDefKeys);
        if(!defKeys.isEmpty()){
            for(String key : defKeys){
                DefinitionField definitionField = (DefinitionField) updatedObj.getFieldByName(key);
                JCRDefinition innerDefinition = (JCRDefinition) definitionField.getDefinition();
                // check whether it has this field or not. In case of update this node would already been b

                Node innerDefNode = JcrDaoUtils.addNode(existingNode, JcrNameFac.getUserName(definitionField.getName()));
                if(definitionField.getValueType().isArray()) {
                    JcrDaoUtils.setProperty(innerDefNode, FixedNames.defSize(), definitionField.getArraySize());
                }
                updateDefToNode(innerDefinition, innerDefNode, session);
                JcrDaoUtils.setProperty(innerDefNode, FixedNames.field(), serialization.serialize(definitionField) );
            }
        }
    }



    private void updateDefToNode(JCRDefinition updatedObj, Node existingNode, Session session) throws
            RepositoryException, IOException {

        JcrDaoUtils.setProperty(existingNode, FixedNames.name(), updatedObj.getName());
        JcrDaoUtils.setProperty(existingNode, FixedNames.desc(), updatedObj.getDescription());
        JcrDaoUtils.setProperty(existingNode, FixedNames.createAt(),  JcrDaoUtils.createCalender(updatedObj
                .getCreateDate()));
        updateDefToNodeNormalField(updatedObj, existingNode, session);
        updateDefToNodeDefinitionField(updatedObj, existingNode, session);
    }

    @Override
    public JCRDefinition get(Serializable id) throws DatabaseException {
        Session session = null;
        try {
            JCRDefinition definition = new JCRDefinition();
            session = jcriRepository.getSession();
            copyNodeToDef(session.getNode((String) id), definition);
            session.save();
            return definition;
        } catch (Exception e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
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
            session = jcriRepository.getSession();
            session.removeItem((String) id);
            session.save();
        } catch (RepositoryException e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }

    @Override
    public boolean exists(Serializable id) throws DatabaseException {
        Session session = null;
        try {
            session = jcriRepository.getSession();
            return session.nodeExists((String) id);
        } catch (RepositoryException e) {
            throw new DatabaseException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }


    /**
     *
     * @param definition
     * @param node
     * @param session
     * @throws RepositoryException
     * @throws JsonProcessingException
     */
    private void copyDefToNode(JCRDefinition definition, Node node, Session session) throws RepositoryException,
            JsonProcessingException {

        JcrDaoUtils.setProperty(node, FixedNames.name(), definition.getName());
        JcrDaoUtils.setProperty(node, FixedNames.desc(), definition.getDescription());
        JcrDaoUtils.setProperty(node, FixedNames.createAt(), JcrDaoUtils.createCalender(definition.getCreateDate()));

        Node fieldNode = JcrDaoUtils.createIfNotExist(node, FixedNames.fields());

        for (Field field : definition.getFields()) {
            if(field.getValueType().isDefinition()){
                DefinitionField definitionField = (DefinitionField) field;
                JCRDefinition innerDefinition = (JCRDefinition) definitionField.getDefinition();
                // check whether it has this field or not. In case of update this node would already been b

                Node innerDefNode = JcrDaoUtils.addNode(node, JcrNameFac.getUserName(definitionField.getName()));
                if(field.getValueType().isArray()) {
                    JcrDaoUtils.setProperty(innerDefNode, FixedNames.defSize(), field.getArraySize());
                }
                copyDefToNode(innerDefinition, innerDefNode, session);
                JcrDaoUtils.setProperty(innerDefNode, FixedNames.field(), serialization.serialize(field) );
            } else {
                JcrDaoUtils.setProperty(fieldNode, JcrNameFac.getUserName(field.getName()), serialization.serialize(field));
            }
        }
    }


    /**
     *
     * @param node
     * @param definition
     * @throws RepositoryException
     * @throws IOException
     */
    private void copyNodeToDef(Node node, JCRDefinition definition) throws RepositoryException, IOException {
        definition.set__id(node.getPath());
        definition.setDescription(JcrDaoUtils.getProperty(node, FixedNames.desc()).getString());
        definition.setName(JcrDaoUtils.getProperty(node, FixedNames.name()).getString());
        definition.setCreateDate( JcrDaoUtils.getProperty(node, FixedNames.createAt()).getDate().getTime() );
        Node fieldNode = JcrDaoUtils.getNode(node, FixedNames.fields());

        if(fieldNode != null){
            PropertyIterator propertyIterator = fieldNode.getProperties( JcrNameFac.getUserName("*").pattern());
            while (propertyIterator.hasNext()) {
                Property prop = propertyIterator.nextProperty();
                Field field = serialization.deserialize(prop.getString(), Field.class);
                definition.getFields().add(field);
            }
            NodeIterator defNodeIterator = node.getNodes(JcrNameFac.getUserName("*").pattern());
            while (defNodeIterator.hasNext()){
                Node innerDefNode = defNodeIterator.nextNode();
                JCRDefinition childDefinition = new JCRDefinition();
                DefinitionField definitionField =  serialization.deserialize( JcrDaoUtils.getProperty(innerDefNode,
                        FixedNames.field() ).getString(), DefinitionField.class );
                definitionField.setDefinition(childDefinition);
                copyNodeToDef(innerDefNode, childDefinition);
                definition.getFields().add(definitionField);
            }
        }
    }
}
