package com.astro.core.script.player;


import com.astro.core.script.util.PopupMsg;
import common.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class PopupMsgTest {

    @Test
    public void shouldBeQueuePolled() {
        //given
        final PopupMsg popupMsg = new PopupMsg();

        //when
        popupMsg.addMessagesToQueue("test");
        popupMsg.update(1000);

        //then
        Assert.assertNotNull("Label object should be created", popupMsg.messagesQueue.size() == 0);
        Assert.assertNotNull("Time should be 0", popupMsg.currentMsgTime == 0f);
    }
}
