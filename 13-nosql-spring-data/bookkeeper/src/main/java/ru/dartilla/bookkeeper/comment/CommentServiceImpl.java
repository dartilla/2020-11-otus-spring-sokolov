package ru.dartilla.bookkeeper.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.comment.vo.CommentInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentNode;
import ru.dartilla.bookkeeper.comment.vo.CommentTree;
import ru.dartilla.bookkeeper.domain.Comment;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.exception.CommentIsNotFound;
import ru.dartilla.bookkeeper.exception.ScriptIsNotFound;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.repositores.CommentRepository;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ScriptService scriptService;

    public CommentTree findByScript(String scriptId) {
        List<CommentNode> rootNodeList = new ArrayList<>();
        Script script = scriptService.findById(scriptId).orElseThrow(ScriptIsNotFound::new);
        Collection<Comment> allComments = commentRepository.findByScriptId(scriptId).stream().sorted(Comparator.comparing(Comment::getId))
                .collect(toList());
        Map<String, CommentNode> nodeByIdIndex = allComments.stream().collect(toMap(Comment::getId,
                vo -> new CommentNode(vo.getId(), vo.getMessage(), new ArrayList<>())));
        for (Comment dbVo : allComments) {
            CommentNode node = nodeByIdIndex.get(dbVo.getId());
            if (dbVo.getParent() == null) {
                rootNodeList.add(node);
            } else {
                nodeByIdIndex.get(dbVo.getParent().getId()).getChildren().add(node);
            }
        }
        return new CommentTree(script, rootNodeList);
    }

    @Override
    public void addComment(CommentInsertVo voToInsert) {
        Script script = scriptService.findById(voToInsert.getScriptId()).orElseThrow(ScriptIsNotFound::new);
        Comment parentComment = voToInsert.getParentId() == null ? null
                : commentRepository.findById(voToInsert.getParentId()).orElseThrow(CommentIsNotFound::new);
        commentRepository.save(new Comment(null, script, parentComment, voToInsert.getMessage()));
    }
}
