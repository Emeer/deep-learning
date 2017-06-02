package com.eztech.deep.learning.kafka;

import java.io.File;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jia on 01/06/2017.
 */
@Data
@NoArgsConstructor
public class Detection {

    private DetectionType type;

    private File file;

    private String text;

    private String result;

}
