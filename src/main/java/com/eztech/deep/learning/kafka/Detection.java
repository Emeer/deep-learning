package com.eztech.deep.learning.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * Object Detection.
 * <p>
 * Created by jia on 01/06/2017.
 */
@Data
@NoArgsConstructor
public class Detection {

    /** . */
    private DetectionType type;

    /** . */
    private File file;

    /** . */
    private String text;

    /** . */
    private String result;

}
