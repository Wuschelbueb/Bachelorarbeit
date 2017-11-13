/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bachelor.util;

import javafx.scene.image.Image;

/**
 * creates a class which holds the Image and the Name of the file. like that we
 * don't need to save the image on the harddisk.
 *
 * @author David
 */
public class WrapperImageName {

    private Image image = null;
    private String fileName = null;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
