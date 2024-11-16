package com.example.books_service.service.imp;

import com.example.books_service.dto.request.AuthorsRequest;
import com.example.books_service.dto.request.PublishersRequest;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.dto.response.PublishersResponse;
import com.example.books_service.repository.PublishersServiceRepository;
import com.example.books_service.service.PublishersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublishersServiceImp implements PublishersService {

    private PublishersServiceRepository publishersServiceRepository;

    @Autowired
    public PublishersServiceImp(PublishersServiceRepository publishersServiceRepository) {
        this.publishersServiceRepository = publishersServiceRepository;
    }

    @Override
    public PageResponse<PublishersResponse> findAll(String name, String phone, int pageNum, int pageSize) {
        return publishersServiceRepository.findAll(name,phone,pageNum,pageSize);
    }

    @Override
    public PublishersRequest save(PublishersRequest publishersRequest) {
        return publishersServiceRepository.save(publishersRequest);
    }

    @Override
    public PublishersRequest update(Integer id, PublishersRequest publishersRequest) {
        return publishersServiceRepository.update(id,publishersRequest);
    }

    @Override
    public void delete(Integer id) {

    }
}
