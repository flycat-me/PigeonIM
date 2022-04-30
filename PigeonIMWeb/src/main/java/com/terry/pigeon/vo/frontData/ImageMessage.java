package com.terry.pigeon.vo.frontData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author terry_lang
 * @description
 * @since 2022-03-27 17:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageMessage {
    private Long receiverId;
    private Integer channelType;
    private MultipartFile image;
}
