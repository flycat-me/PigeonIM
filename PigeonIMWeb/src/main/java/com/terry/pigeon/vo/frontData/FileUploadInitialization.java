package com.terry.pigeon.vo.frontData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-28 20:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadInitialization {
    private String fileName;
    private String fileSize;
}
