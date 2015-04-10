package uk.gov.prototype.vitruvius.parser;

import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;

import java.util.Date;

public class DateParser {


    public static String parse(Date date) {
        return ISODateTimeFormat.dateHourMinuteSecond().print(date.getTime());
    }

}
