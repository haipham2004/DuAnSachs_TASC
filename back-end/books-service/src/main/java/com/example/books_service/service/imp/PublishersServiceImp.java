package com.example.books_service.service.imp;

import com.example.books_service.dto.response.PublishersResponse;
import com.example.books_service.repository.PublishersServiceRepository;
import com.example.books_service.service.PublishersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishersServiceImp implements PublishersService {

    private PublishersServiceRepository publishersServiceRepository;

    @Autowired
    public PublishersServiceImp(PublishersServiceRepository publishersServiceRepository) {
        this.publishersServiceRepository = publishersServiceRepository;
    }

    @Override
    public List<PublishersResponse> findAllPublisherDto() {
        return publishersServiceRepository.findAllCategoriesDto();
    }
}
