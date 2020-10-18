package com.bit.paperhouse.service;


import com.bit.paperhouse.dao.NoticeRepository;
import com.bit.paperhouse.dto.NoticeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoticeService {

    @Autowired
    NoticeRepository dao;


    public List<NoticeDto> allList() {
        return dao.allList();
    }

    public void writeNoticeAf(NoticeDto dto){

        dao.writeNoticeAf(dto);
    }

    public void deleteNotice(int noticeSeq){

        dao.deleteNotice(noticeSeq);
    }



}
