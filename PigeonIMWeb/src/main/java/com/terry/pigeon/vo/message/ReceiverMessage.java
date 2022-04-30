package com.terry.pigeon.vo.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-27 12:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverMessage {
    private String event;
    private KeyboardMessage data;
}
