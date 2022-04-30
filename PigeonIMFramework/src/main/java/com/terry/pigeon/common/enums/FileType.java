package com.terry.pigeon.common.enums;

import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public enum FileType {

    /**
     * JPEG  (jpg)
     */
    JPEG("JPEG", "FFD8FF"),

    JPG("JPG", "FFD8FF"),

    /**
     * PNG
     */
    PNG("PNG", "89504E47"),

    /**
     * GIF
     */
    GIF("GIF", "47494638"),

    /**
     * TIFF (tif)
     */
    TIFF("TIF", "49492A00"),

    /**
     * Windows bitmap (bmp)
     */
    BMP("BMP", "424D"),

    /**
     * 16色位图(bmp)
     */
    BMP_16("BMP", "424D228C010000000000"),

    /**
     * 24位位图(bmp)
     */
    BMP_24("BMP", "424D8240090000000000"),

    /**
     * 256色位图(bmp)
     */
    BMP_256("BMP", "424D8E1B030000000000"),

    /**
     * XML
     */
    XML("XML", "3C3F786D6C"),

    /**
     * HTML (html)
     */
    HTML("HTML", "68746D6C3E"),

    /**
     * Microsoft Word/Excel 注意：word 和 excel的文件头一样
     */
    XLS("XLS", "D0CF11E0"),

    /**
     * Microsoft Word/Excel 注意：word 和 excel的文件头一样
     */
    DOC("DOC", "D0CF11E0"),

    /**
     * Microsoft Word/Excel 2007以上版本文件 注意：word 和 excel的文件头一样
     */
    DOCX("DOCX", "504B0304"),

    /**
     * Microsoft Word/Excel 2007以上版本文件 注意：word 和 excel的文件头一样 504B030414000600080000002100
     */
    XLSX("XLSX", "504B0304"),

    /**
     * Adobe Acrobat (pdf) 255044462D312E
     */
    PDF("PDF", "25504446");

    /**
     * 后缀 大写字母
     */
    private final String suffix;

    /**
     * 魔数
     */
    private final String magicNumber;

    FileType(String suffix, String magicNumber) {
        this.suffix = suffix;
        this.magicNumber = magicNumber;
    }

    @NonNull
    public static FileType getBySuffix(String suffix) {
        for (FileType fileType : values()) {
            if (fileType.getSuffix().equals(suffix.toUpperCase())) {
                return fileType;
            }
        }
        throw new IllegalArgumentException("不支持的文件后缀 : " + suffix);
    }
}