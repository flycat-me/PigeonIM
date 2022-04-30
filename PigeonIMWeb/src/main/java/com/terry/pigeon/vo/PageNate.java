package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-13 17:30
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageNate {
    private Long page;

    private Long size;

    private Long total;
}
