package com.smalltiantian.genetics;

import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

@Test
public class PictureTest {
    private static final String FOLDER = "temporary_folder";

    @AfterTest
    public void cleanFolder() {
        File folder = new File(FOLDER);
        if (folder.exists()) {
            folder.delete();
        }
    }

    public void accessSavePicture() throws Exception {
        Map<String, String> extraConfig = new HashMap<String, String>();
        extraConfig.put("savePath", FOLDER);

        File folder = new File(FOLDER);
        Assert.assertEquals(folder.list().length, 0);

        Picture pic = new Picture(100);
        pic.draw();

        Assert.assertEquals(folder.list().length, 1);
    }
}
