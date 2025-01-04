package com.example.demo.notice.service;

import com.example.demo.member.entity.Member;
import com.example.demo.notice.dto.NoticeDto;
import com.example.demo.notice.entity.Notice;
import com.example.demo.notice.repository.NoticeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    public Notice add(@Valid NoticeDto noticeDto, Member member) {

        Notice notice = Notice.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .member(member)
                .build();
        return noticeRepository.save(notice);
    }
}
