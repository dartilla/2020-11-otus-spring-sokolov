package ru.dartilla.bookkeeper.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.bookkeeper.comment.vo.CommentInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentNode;
import ru.dartilla.bookkeeper.comment.vo.CommentTree;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.domain.Comment;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.repositores.CommentRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("Service работы с комментариями книги должен")
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ScriptService scriptService;

    private CommentServiceImpl commentService;

    @BeforeEach
    private void setUp() {
        commentService = new CommentServiceImpl(commentRepository, scriptService);
    }

    @DisplayName("находить комментарии для книги")
    @Test
    public void shouldFindByBook() {
        Author author = new Author(22L, "Неизветсных О.");
        Script script = new Script(100L, "моя книга", author, null);
        Comment first = new Comment(1L, script, null, "first");
        Comment firstChild = new Comment(2L, script, first, "firstChild");
        Comment secondChild = new Comment(3L, script, first, "secondChild");
        Comment firstGrand = new Comment(4L, script, firstChild, "firstGrand");
        Comment second = new Comment(5L, script, null, "second");
        when(commentRepository.findByScriptId(script.getId())).thenReturn(Arrays.asList(first, firstChild, secondChild, firstGrand, second));
        when(scriptService.findById(script.getId())).thenReturn(Optional.of(script));

        CommentTree commentTree = commentService.findByScript(script.getId());
        List<CommentNode> comments = commentTree.getNodes();
        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments).usingElementComparator(Comparator.comparing(CommentNode::getMessage))
                .containsOnly(new CommentNode(first.getId(), first.getMessage(), null),
                        new CommentNode(second.getId(), second.getMessage(), null));
        assertThat(comments.get(0).getChildren())
                .usingElementComparator(Comparator.comparing(CommentNode::getId).thenComparing(CommentNode::getMessage))
                .containsOnly(new CommentNode(firstChild.getId(), firstChild.getMessage(), null),
                        new CommentNode(secondChild.getId(), secondChild.getMessage(), null));
        assertThat(comments.get(0).getChildren().get(0).getChildren().get(0)).isEqualToComparingOnlyGivenFields(
                new CommentNode(firstGrand.getId(), firstGrand.getMessage(), null), "message");
    }

    @DisplayName("добавлять первый комментарий")
    @Test
    public void shouldAddRootComment() {
        Script script = new Script(100L, "моя книга", null, null);
        Comment newComment = new Comment(null, script, null, "new");
        when(scriptService.findById(script.getId())).thenReturn(Optional.of(script));
        commentService.addComment(new CommentInsertVo(script.getId(), null, newComment.getMessage()));
        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualToComparingOnlyGivenFields(newComment, "message");
        assertThat(captor.getValue().getScript()).isEqualToComparingOnlyGivenFields(script, "id", "title");
    }

    @DisplayName("добавлять дочерний комментарий")
    @Test
    public void shouldAddChildComment() {
        Script script = new Script(100L, "моя книга", null, null);
        Comment parentComment = new Comment(2L, script, null, "root");
        Comment newComment = new Comment(3L, script, parentComment, "new");
        when(scriptService.findById(script.getId())).thenReturn(Optional.of(script));
        when(commentRepository.findById(parentComment.getId())).thenReturn(Optional.of(parentComment));

        commentService.addComment(new CommentInsertVo(script.getId(), parentComment.getId(), newComment.getMessage()));
        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue()).isEqualToComparingOnlyGivenFields(newComment, "message");
        assertThat(captor.getValue().getScript()).isEqualToComparingOnlyGivenFields(script, "id", "title");
        assertThat(captor.getValue().getParent()).isEqualToComparingOnlyGivenFields(parentComment, "id", "message");
    }
}