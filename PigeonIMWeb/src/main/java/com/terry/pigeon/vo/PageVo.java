package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-13 17:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {
    private List rows;
    private PageNate pageNate;
}
