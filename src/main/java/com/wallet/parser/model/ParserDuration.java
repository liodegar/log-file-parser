package com.wallet.parser.model;

import com.wallet.parser.util.EnumConverter;
import com.wallet.parser.util.ReverseEnumMap;

/**
 * Created by Liodegar on 11/20/2018.
 */
public enum ParserDuration implements EnumConverter<ParserDuration> {
    HOURLY(1, "hourly"), DAILY(2, "daily");

    private static ReverseEnumMap<ParserDuration> map = new ReverseEnumMap<>(ParserDuration.class);
    private final byte id;
    private final String code;

    ParserDuration(int id, String code) {
        this.id = (byte) id;
        this.code = code;
    }

    public byte getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    @Override
    public ParserDuration convert(byte val) {
        return getFromValue(val);
    }

    @Override
    public ParserDuration convert(String code) {
        return getFromCode(code);
    }

    public static ParserDuration getFromValue(int val) {
        return map.get((byte) val);
    }

    public static ParserDuration getFromCode(String code) {
        return map.get(code);
    }

}
