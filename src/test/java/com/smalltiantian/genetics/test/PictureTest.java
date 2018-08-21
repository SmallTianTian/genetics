package com.smalltiantian.genetics;

import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

@Test
public class PictureTest {
    private static final String FOLDER = "temp";

    @AfterTest
    public void cleanFolder() {
        File folder = new File(FOLDER);
        if (folder.exists()) {
            for (File f : folder.listFiles()) {
                f.delete();
            }
            folder.delete();
        }
    }

    public void accessSavePicture() throws Exception {
        Map<String, String> extraConfig = new HashMap<String, String>();
        extraConfig.put("savePath", FOLDER);

        Earth earth = TestHelper.produceEarth(extraConfig);
        File folder = new File(FOLDER);
        folder.mkdirs();
        Assert.assertEquals(folder.list().length, 0);

        Picture pic = new Picture(100, earth);
        pic.draw();

        Assert.assertEquals(folder.list().length, 1);
    }
}
