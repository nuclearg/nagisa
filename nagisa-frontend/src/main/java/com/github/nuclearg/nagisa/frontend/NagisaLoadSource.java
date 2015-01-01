package com.github.nuclearg.nagisa.frontend;

import static com.github.nuclearg.nagisa.frontend.util.NagisaStrings.LN;

import org.apache.commons.lang3.StringUtils;

/**
 * 待加载的源文件
 * 
 * @author ng
 *
 */
public final class NagisaLoadSource {
    /**
     * 被解析的源代码
     */
    private final String code;
    /**
     * 文件名
     */
    private final String filename;

    public NagisaLoadSource(String code, String filename) {
        if (code == null)
            throw new IllegalArgumentException("code is null");
        if (StringUtils.isBlank(filename))
            throw new IllegalArgumentException("filename is blank");

        this.code = code + LN;
        this.filename = filename;
    }

    /** 被解析的源代码 */
    public String getCode() {
        return this.code;
    }

    /** 文件名 */
    public String getFilename() {
        return this.filename;
    }
}
