package com.jfcheng.json.parse;

import java.io.Serializable;

/**
 * Created by jfcheng on 4/24/16.
 * <p>
 * This is the common interface of a JSON value.
 */
public interface JsonValue extends Serializable {
    static final boolean DEFAULT_IGNORE_NULL_FIELD = true;

    /**
     * Get the actual value of the json value is representing.
     * <p>
     * The actual value is mapping to a java type.
     * JsonObject -> Map
     * JsonArray  -> List
     * JsonString -> String
     * JsonNumber -> Number(default to be Long or Double)
     * JsonBoolean-> Boolean
     * JsonNull   -> null
     *
     * @return Object.
     */
    public Object getValue();


    /**
     * Return a json text.
     *
     * @return - String
     */
    public Object toJsonText(boolean ignoreNullField);

    /**
     * Default to return a json text with a ignoredNullField is true;
     * @return -String
     */
    public default Object toJsonText(){
        return toJsonText(DEFAULT_IGNORE_NULL_FIELD);
    }





}
