package com.terry.pigeon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-22 15:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRecordsVo {
    private List rows;
    private Integer messageId;
    private Integer limit;
}
