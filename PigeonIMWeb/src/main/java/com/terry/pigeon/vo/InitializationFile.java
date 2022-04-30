package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-28 20:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitializationFile {

    private String uploadId;

    private String splitSize;
}
