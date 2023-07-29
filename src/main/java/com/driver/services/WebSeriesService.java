package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.Transformer.WebseriesTransformer;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()) != null){
            throw new Exception("Series is already present");
        }

        WebSeries webSeries = WebseriesTransformer.webSeriesDtoToWebseries(webSeriesEntryDto);
        Optional<ProductionHouse> optionalProductionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        ProductionHouse productionHouse = optionalProductionHouse.get();
        webSeries.setProductionHouse(productionHouse);
        WebSeries savedWebseries = webSeriesRepository.save(webSeries);

        if(productionHouse.getWebSeriesList().size() == 0){
            productionHouse.setRatings(savedWebseries.getRating());
        }else{
            double ratings = (productionHouse.getRatings() * productionHouse.getWebSeriesList().size()) + savedWebseries.getRating();
            productionHouse.setRatings(ratings / (productionHouse.getWebSeriesList().size() + 1));
        }
        productionHouse.getWebSeriesList().add(savedWebseries);
        productionHouseRepository.save(productionHouse);

        return savedWebseries.getId();
    }

}
