package com.astro.core.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

/**
 * Test of the  MessagesManager.
 */
public class MessagesManagerTest {

    Properties plMsg = new Properties();

    @Before
    public void init() {
        plMsg.setProperty("testKey", "testVal");
    }

    @Test
    public void shouldHaveCorrectProperties() {
        //given
        MessagesManager manager = new MessagesManager();

        //when
        manager.addLanguages("pl", plMsg);

        //then
        Assert.assertTrue("Default languages should be PL", "pl".equals(manager.getSelectedLanguages()));
        Assert.assertTrue("Messages should be return", !"".equals(manager.getMsg("testKey")));
    }

}
