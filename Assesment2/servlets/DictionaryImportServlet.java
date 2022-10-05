package com.akqa.aem.training.aem201.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.text.csv.Csv;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes="aem201/components/page",
        methods=HttpConstants.METHOD_GET,
        selectors = "import.dictionary",
        extensions="html")
@ServiceDescription("CSV Dictionary upload Servlet")
public class DictionaryImportServlet extends SlingSafeMethodsServlet {
    private static final String BASE_PATH = "/dictionarydata/dictionary1";
    public static Map<Integer, String> headerRowPropertyMap;
    private Map<String, Map<String, String>> dictionaryMap;
    private boolean flag;
    private void toggleFlag() {
        flag = false;
    }

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Logger logger = LoggerFactory.getLogger(getClass());
        final Resource resource = req.getResource();

        resp.setContentType("text/plain");
        resp.getWriter().write("Title = " + resource.getValueMap().get(JcrConstants.JCR_TITLE) + " success");

        //Create a Session
        Session session = resource.getResourceResolver().adaptTo(Session.class);
        try {
            final Csv csv = new Csv();
            Resource res = resource.getResourceResolver().getResource("/content/dam/sourav-dam/my.csv"); // from DAM storage
            Asset asset = res.adaptTo(Asset.class);
            Rendition rendition = asset.getOriginal();
            InputStream inputStream = rendition.adaptTo(InputStream.class);
            final Iterator<String[]> rows = csv.read(inputStream, "UTF-8");
            if (Objects.nonNull(session) && session.nodeExists(BASE_PATH)) {
                final Node rootNode = session.getNode(BASE_PATH); // fetching node at where we need to add the data
                JcrUtils.getOrAddNode(rootNode, JcrConstants.JCR_CONTENT, JcrConstants.NT_UNSTRUCTURED);

                // map for which property lie at which index of csv
                headerRowPropertyMap = new HashMap<>();
                // map for words to its properties
                dictionaryMap = new HashMap<>();
                // for checking whether the first row(header) of the CSV is stored or not
                flag = true;

                rows.forEachRemaining(row -> {
                    final List<String> dataList = Arrays.asList(row);
                    logger.info("String row:", dataList);
                    // if header is not stored //
                    if(flag) {
                        headerRowPropertyMap.put(0, "jcr:title"); // wordName is added as the title property
                        for(int index = 1; index<dataList.size(); index++) {
                            headerRowPropertyMap.put(index, dataList.get(index));
                        }
                        // if header is stored then toggle so that next time we can store words
                        toggleFlag();
                    } else {
                        fillMap(dataList);
                    }
                });
                // importing words into a specific node
                for(String word : dictionaryMap.keySet()) {
                    try {
                        importWords(word, dictionaryMap.get(word), rootNode);
                        session.save();
                    } catch (RepositoryException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            log(e.getMessage());
        }
    }

    // filling the word and it's properties
    private void fillMap(List<String> row) {
        Map<String, String> tempMap = new HashMap<>(); // temporary map for mapping the properties
        for(int index=0; index < row.size(); index++) {
            tempMap.put(headerRowPropertyMap.get(index), row.get(index));
        }
        dictionaryMap.put(row.get(0), tempMap); // adding the tempMap(this word's properties) infront of itself
    }

    // importing words into a specific node
    private void importWords(String word, Map<String, String> map, Node dictionaryNode) {
        try {
            final Node wordNode = JcrUtils.getOrAddNode(dictionaryNode, word, JcrConstants.NT_UNSTRUCTURED); // creating a node for a specific word
            for(String property : map.keySet()) {
                wordNode.setProperty(property, map.get(property));  // adding its properties
            }
        } catch (RepositoryException e) {
            log("Error: ", e);
        }
    }
}