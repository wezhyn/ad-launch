package com.ad.admain.controller.pay.to;

import com.wezhyn.project.utils.Strings;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wezhyn
 * @since 02.25.2020
 */
public class ProduceContextConvert implements AttributeConverter<List<String>, String> {


    public static final String DELIMITER="`";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute==null) {
            return "";
        }
        return String.join(DELIMITER, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (Strings.isEmpty(dbData)) {
            return Collections.emptyList();
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .collect(Collectors.toList());
    }

}
