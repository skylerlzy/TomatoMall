package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Comment;
import com.example.tomatomall.repository.CommentRepository;
import com.example.tomatomall.service.CommentService;
import com.example.tomatomall.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentVO add(CommentVO commentVO) {
        Comment comment = convertVoToEntity(commentVO);
        Comment savedComment = commentRepository.save(comment);

        // 处理父子评论关系
        if (savedComment.getFatherId() != null) {
            commentRepository.appendSon(savedComment.getFatherId(), savedComment.getId());
        }

        return convertEntityToVo(savedComment);
    }

    @Override
    @Transactional
    public String delete(Integer id) {
        return commentRepository.findById(id)
                .map(comment -> {
                    commentRepository.deleteById(id); // 级联删除由数据库处理
                    return "评论删除成功";
                })
                .orElseThrow(TomatoMallException::commentNotFound);
    }

    @Override
    @Transactional
    public String update(Integer id, CommentVO commentVO) {
        return commentRepository.findById(id)
                .map(existing -> {
                    BeanUtils.copyProperties(commentVO, existing, "id", "productId", "userId", "fatherId");
                    commentRepository.save(existing);
                    return "评论更新成功";
                })
                .orElseThrow(TomatoMallException::commentNotFound);
    }

    @Override
    public List<CommentVO> getAll() {
        return commentRepository.findAll().stream()
                .map(this::convertEntityToVo)
                .collect(Collectors.toList());
    }

    @Override
    public CommentVO get(Integer id) {
        return commentRepository.findById(id)
                .map(this::convertEntityToVo)
                .orElseThrow(TomatoMallException::commentNotFound);
    }

    // VO/Entity 转换方法
    private Comment convertVoToEntity(CommentVO vo) {
        Comment entity = new Comment();
        BeanUtils.copyProperties(vo, entity);
        // 处理可能为空的集合
        entity.setSon(Optional.ofNullable(vo.getSon()).orElse(Collections.emptyList()));
        return entity;
    }

    private CommentVO convertEntityToVo(Comment entity) {
        CommentVO vo = new CommentVO();
        BeanUtils.copyProperties(entity, vo);
        // 确保子评论列表不为null
        vo.setSon(Optional.ofNullable(entity.getSon()).orElse(Collections.emptyList()));
        return vo;
    }
}