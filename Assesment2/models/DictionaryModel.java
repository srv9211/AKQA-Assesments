package com.akqa.aem.training.aem201.core.models;

import com.akqa.aem.training.aem201.core.schedulers.DictionaryScheduler;

import com.akqa.aem.training.aem201.core.servlets.DictionaryImportServlet;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;
@Model(adaptables = Resource.class)
public class DictionaryModel {
    private List<Node> nodesList = new ArrayList<>();
    private int randomNumber;
    public static int dictionarySize;

    @SlingObject
    private ResourceResolver resourceResolver;

    final Logger logger = LoggerFactory.getLogger(getClass());
    @PostConstruct
    protected void init() throws RepositoryException {
        //Create a Session
        Session session = resourceResolver.adaptTo(Session.class);

        try {
            Node dictionary = session.getNode("/dictionarydata/dictionary1"); // fetching the dictionary node
            NodeIterator nodeIterator = dictionary.getNodes();

            while(nodeIterator.hasNext()) {
                nodesList.add(nodeIterator.nextNode()); // adding all nodes in a List so that we can get a random index.
            }                                           // here you can do CRUD operations at the node.
            dictionarySize = nodesList.size();
            String tempName;
            do {
                randomNumber = DictionaryScheduler.randomIndex;     // fetching a ranndomIndex with the help of scheduler
                tempName = nodesList.get(randomNumber).getName();   // avoiding the "jcr:content" node
            }
            while((tempName.equals("jcr:content")));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public String getTime() {
        return DictionaryScheduler.wordUpdatedTime;
    }
    public String getWord() throws RepositoryException {
        try {
            return nodesList.get(randomNumber).getProperty("jcr:title").getValue().toString();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    public String getType() throws RepositoryException {
        try {
            return nodesList.get(randomNumber).getProperty("type").getValue().toString();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    public String getMeaning() throws RepositoryException {
        try {
            return nodesList.get(randomNumber).getProperty("meaning").getValue().toString();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
    public String getSentence() throws RepositoryException {
        try {
            return nodesList.get(randomNumber).getProperty("sentence").getValue().toString();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}