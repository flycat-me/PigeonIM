package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-28 22:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergeFile {
    private String uploadId;
    private String isMerge;
}
