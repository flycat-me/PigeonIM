package com.terry.pigeon.vo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-24 13:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TalkMessage<T> {
    private String event;
    private T content;

}
