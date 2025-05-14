package com.example.tomatomall.controller;

import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.vo.CommentVO;
import com.example.tomatomall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/api/comment/add")
    public ResultVO<CommentVO> add(@RequestBody CommentVO vo) {
        CommentVO commentVO = commentService.add(vo);
        return ResultVO.buildSuccess(commentVO);
    }

    @DeleteMapping("/api/comment/{id}")
    public ResultVO<String> delete(@PathVariable Integer id) {
        String s = commentService.delete(id);
        return ResultVO.buildSuccess(s);
    }

    @PutMapping("/api/comment/{id}")
    public ResultVO<String> update(@PathVariable Integer id, @RequestBody CommentVO vo) {
        String s = commentService.update(id, vo);
        return ResultVO.buildSuccess(s);
    }

    @GetMapping("/api/comment/getAll")
    public ResultVO<List<CommentVO>> getAll() {
        return ResultVO.buildSuccess(commentService.getAll());
    }

    @GetMapping("/api/comment/{id}")
    public ResultVO<CommentVO> get(@PathVariable Integer id) {
        return ResultVO.buildSuccess(commentService.get(id));
    }
}