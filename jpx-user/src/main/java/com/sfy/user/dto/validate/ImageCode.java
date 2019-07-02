package com.sfy.user.dto.validate;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * Created by 金鹏祥 on 2019/5/14.
 */
@Data
public class ImageCode {

    private BufferedImage image;
    private String code;

    public ImageCode(BufferedImage image, String code){
        this.image = image;
        this.code = code;
    }
}

