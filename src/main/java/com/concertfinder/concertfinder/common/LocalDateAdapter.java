package com.concertfinder.concertfinder.common;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Override
    public LocalDate unmarshal(String date) throws Exception {
        return LocalDate.parse(date, formatter);
    }

    @Override
    public String marshal(LocalDate date) throws Exception {
        return date.format(formatter);
    }
}
