package com.example.demo.Service;

import com.example.demo.DTO.Request.FineReceiptRequest;
import com.example.demo.DTO.Response.FineReceiptResponse;
import com.example.demo.Mapper.FineReceiptMapper;
import com.example.demo.Repository.Entity.FineReceiptEntity;
import com.example.demo.Repository.Entity.ReaderEntity;
import com.example.demo.Repository.IRepository.FineReceiptRepository;
import com.example.demo.Repository.IRepository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FineReceiptService {
    @Autowired
    FineReceiptRepository fineReceiptRepository;

    @Autowired
    FineReceiptMapper fineReceiptMapper;

    @Autowired
    ReaderRepository readerRepository;

    public FineReceiptResponse createFineReceipt(FineReceiptRequest fineReceiptRequest, Integer readerId) {
        ReaderEntity readerEntity = readerRepository.findById(readerId).get();
        FineReceiptEntity fineReceiptEntity = fineReceiptMapper.toEntity(fineReceiptRequest);
        fineReceiptEntity.setReader(readerEntity);
        readerEntity.getFineReceipts().add(fineReceiptEntity);
        fineReceiptRepository.save(fineReceiptEntity);
        return fineReceiptMapper.toResponse(fineReceiptEntity);
    }


    public List<FineReceiptResponse> getFineReceipt(Integer id) {
        ReaderEntity readerEntity = readerRepository.findById(id).get();
        List<FineReceiptEntity> fineReceiptEntity = readerEntity.getFineReceipts();
        List<FineReceiptResponse> fineReceiptResponses = new ArrayList<>();
        for (FineReceiptEntity fineReceiptEntity1 : fineReceiptEntity) {
            fineReceiptResponses.add(fineReceiptMapper.toResponse(fineReceiptEntity1));
        }
        return fineReceiptResponses;
    }
}
