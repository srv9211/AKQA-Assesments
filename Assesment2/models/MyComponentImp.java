package com.akqa.aem.training.aem201.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = MyComponentInterface.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL // what ever fields are there in the dialog box they are optional or required
)
public class MyComponentImp implements MyComponentInterface {
    private static final Logger LOG = LoggerFactory.getLogger(MyComponentImp.class);

    @ScriptVariable
    Page currentPage;

    @Inject // is used to link the dialog box field and fields names must be same. Basically it is linking this variable and field in dialog box
    @Via("resource")
    @Required
    @Default(values = "AEM")
    String firstName;

    @Inject
    @Via("resource")
    @Default(values = "Component") // the default value you can say, if there is not any value it will be shown there
    String lastName;

    @Inject
    @Via("resource")
    @Default(values = "Chamba")
    String address;

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getPageTitle() {
        return currentPage.getTitle();
    }
}
