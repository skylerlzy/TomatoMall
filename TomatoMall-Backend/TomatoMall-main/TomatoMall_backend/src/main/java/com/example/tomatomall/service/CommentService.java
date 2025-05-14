package com.example.tomatomall.service;

import com.example.tomatomall.vo.CommentVO;

import java.util.List;

public interface CommentService {
    CommentVO add(CommentVO comment);
    String delete(Integer id);
    String update(Integer id, CommentVO comment);
    List<CommentVO> getAll();
    CommentVO get(Integer id);
}
