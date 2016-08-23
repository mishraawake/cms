package com.sp.helper;

import com.sp.dao.api.DatabaseException;
import com.sp.dao.api.DefinitionDao;
import com.sp.dao.api.ItemDao;
import com.sp.dao.api.JCRIRepository;
import com.sp.model.*;
import com.sp.service.RunTimeContext;
import com.sp.service.impl.SimpleSubjectProvider;
import com.sp.utils.SpringInitializer;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by pankajmishra on 19/08/16.
 * This is main method that will create test data .. it will be generally used for integration test and that's why
 * it is not added into unit test.
 */
public class MassiveDataBuilder {

    // create five type of definition


    ItemDao<IItem> itemDao;
    DefinitionDao<IDefinition> definitionDao;
    PojoFactory pojoFactory;

    IDefinition channelDefinition;
    IDefinition sectionDefinition;
    IDefinition pollDefinition;
    IDefinition imageGalleryDef;
    IDefinition articleDefinition;
    JCRIRepository repository;


    public void createDefinitions() throws DatabaseException {
        channelDefinition = definitionDao.create(DefUtils.getChannel(pojoFactory));
        sectionDefinition = definitionDao.create(DefUtils.getSection(pojoFactory));
        pollDefinition = definitionDao.create(DefUtils.getPoll(pojoFactory));
        imageGalleryDef = definitionDao.create(DefUtils.getGallery(pojoFactory));
        articleDefinition = definitionDao.create(DefUtils.getArticle(pojoFactory));
    }


    private void createChannelItem(int numberOfChannel) throws DatabaseException {
        for (int channelIndex = 0; channelIndex < numberOfChannel; ++channelIndex) {
            IItem<IItem, IDefinition> channelItem = ItemUtils.getDummyItemForDefinition(pojoFactory, channelDefinition);
            channelItem = itemDao.create(channelItem);
            createSection(channelItem, 10);
        }
    }

    private void createSection(IItem<IItem, IDefinition> channelItem, int numberOfSection) throws DatabaseException {
        for (int sectionIndex = 0; sectionIndex < numberOfSection; ++sectionIndex) {
            IItem<IItem, IDefinition> sectionItem = ItemUtils.getDummyItemForDefinition(pojoFactory, sectionDefinition);
            sectionItem = itemDao.createChild(sectionItem, channelItem);
            System.out.println("creating first level section " + sectionIndex + " for channel '" + channelItem
                    .getName() + "'");
            long stime = System.currentTimeMillis();
            createLastLevelSection(sectionItem, 20);
            System.out.println("time took " + (System.currentTimeMillis() - stime));
        }
    }

    private void createSecondLevelSection(IItem<IItem, IDefinition> parentSection, int numberOfSection) throws
            DatabaseException {
        for (int sectionIndex = 0; sectionIndex < numberOfSection; ++sectionIndex) {
            IItem<IItem, IDefinition> sectionItem = ItemUtils.getDummyItemForDefinition(pojoFactory, sectionDefinition);
            sectionItem = itemDao.createChild(sectionItem, parentSection);
            createLastLevelSection(sectionItem, 20);
            //  createThirdLevelSection(sectionItem, 20);
        }
    }

    private void createThirdLevelSection(IItem<IItem, IDefinition> parentSection, int numberOfSection) throws
            DatabaseException {
        for (int sectionIndex = 0; sectionIndex < numberOfSection; ++sectionIndex) {
            IItem<IItem, IDefinition> sectionItem = ItemUtils.getDummyItemForDefinition(pojoFactory, sectionDefinition);
            sectionItem = itemDao.createChild(sectionItem, parentSection);
            // createLastLevelSection(sectionItem, 20);
        }
    }

    private void createLastLevelSection(IItem<IItem, IDefinition> parentSection, int numberOfSection) throws
            DatabaseException {
        for (int sectionIndex = 0; sectionIndex < numberOfSection; ++sectionIndex) {
            IItem<IItem, IDefinition> sectionItem = ItemUtils.getDummyItemForDefinition(pojoFactory, sectionDefinition);
            sectionItem = itemDao.createChild(sectionItem, parentSection);
            createArticle(sectionItem, 20);
        }

        for (int sectionIndex = 0; sectionIndex < numberOfSection; ++sectionIndex) {
            IItem<IItem, IDefinition> sectionItem = ItemUtils.getDummyItemForDefinition(pojoFactory, sectionDefinition);
            sectionItem = itemDao.createChild(sectionItem, parentSection);
            createGallery(sectionItem, 20);
        }

        for (int sectionIndex = 0; sectionIndex < numberOfSection; ++sectionIndex) {
            IItem<IItem, IDefinition> sectionItem = ItemUtils.getDummyItemForDefinition(pojoFactory, sectionDefinition);
            sectionItem = itemDao.createChild(sectionItem, parentSection);
            createPoll(sectionItem, 20);
        }
    }


    private void createArticle(IItem<IItem, IDefinition> parentSection, int numberOfArticle) throws DatabaseException {
        for (int articleIndex = 0; articleIndex < numberOfArticle; ++articleIndex) {
            IItem<IItem, IDefinition> article = ItemUtils.getDummyItemForDefinition(pojoFactory, articleDefinition);
            itemDao.createChild(article, parentSection);
        }
    }

    private void createPoll(IItem<IItem, IDefinition> parentSection, int numberOfPoll) throws DatabaseException {
        for (int pollIndex = 0; pollIndex < numberOfPoll; ++pollIndex) {
            IItem<IItem, IDefinition> poll = ItemUtils.getDummyItemForDefinition(pojoFactory, pollDefinition);
            itemDao.createChild(poll, parentSection);
        }
    }

    private void createGallery(IItem<IItem, IDefinition> parentSection, int numberOfGal) throws DatabaseException {
        for (int galIndex = 0; galIndex < numberOfGal; ++galIndex) {
            IItem<IItem, IDefinition> imageGallery = ItemUtils.getDummyItemForDefinition(pojoFactory, imageGalleryDef);
            itemDao.createChild(imageGallery, parentSection);
        }
    }

    public static void main(String[] args) throws DatabaseException {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringInitializer.class);
        MassiveDataBuilder massiveDataBuilder = new MassiveDataBuilder();
        massiveDataBuilder.itemDao = applicationContext.getBean("itemDao", ItemDao.class);
        massiveDataBuilder.definitionDao = applicationContext.getBean("definitionDao", DefinitionDao.class);
        massiveDataBuilder.pojoFactory = applicationContext.getBean("pojoFactory", PojoFactory.class);
        RunTimeContext<IUser> runTimeContext = applicationContext.getBean(RunTimeContext.class);
        runTimeContext.setValue(SimpleSubjectProvider.CURRENT_LOGGEDIN_USER, UserUtils.getAdminUser
                (massiveDataBuilder.pojoFactory));
        // massiveDataBuilder.initializeCreation();
        massiveDataBuilder.readBuiltData();
    }

    private void readBuiltData() throws DatabaseException {

        List<IItem> channelItems = itemDao.getRootItems();
        for (IItem item : channelItems) {
            printChildRecursive(item);
        }
    }

    private void printChildRecursive(IItem<IItem, IDefinition> item) throws DatabaseException {

        outer:
        for (IItem<IItem, IDefinition> cItem : itemDao.getChildItems(item.get__id())) {
            cItem.getFieldValues().forEach(fieldValue -> {
                if(fieldValue.getField().getValueType().equals(ValueType.ArrayOfImage)){
                    for(BinaryData binary : (BinaryData[])fieldValue.getValue()){
                        try {
                            FileUtils.writeByteArrayToFile(new File("output/image.jpg"), binary.getBytes());
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
            printChildRecursive(cItem);
        }
    }

    private void initializeCreation() throws DatabaseException {
        createDefinitions();
        createChannelItem(5);
    }

}
