package com.driver.Transformer;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.WebSeries;

public class WebseriesTransformer {
    public static WebSeries webSeriesDtoToWebseries(WebSeriesEntryDto webSeriesEntryDto){
        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        return webSeries;
    }
}
