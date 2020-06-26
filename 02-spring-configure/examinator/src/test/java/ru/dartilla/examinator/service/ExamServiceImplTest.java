package ru.dartilla.examinator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.examinator.domain.Excercise;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamServiceImplTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserInterface userInterface;

    @Mock
    private ExerciseProvider exerciseProvider;

    @Test
    public void notValidCountCreation() {
        assertThatThrownBy(() -> new ExamServiceImpl(0, 2, exerciseProvider, authService, userInterface))
                .hasMessageContaining("questionCountInTest must be positive");
        assertThatThrownBy(() -> new ExamServiceImpl(2, -2, exerciseProvider,  authService, userInterface))
                .hasMessageContaining("rightAnswersToPassExam must be positive");
        assertThat(new ExamServiceImpl(5, 3, exerciseProvider, authService, userInterface)).isNotNull();
    }

    @Test
    public void doExamPass() {
        Set<String> answer = Stream.of("myAnswer").collect(toSet());
        Excercise anyExcercise = mock(Excercise.class);

        when(anyExcercise.getRightAnswers()).thenReturn(answer);
        when(userInterface.readAnswer()).thenReturn(answer);

        when(exerciseProvider.hasNext()).thenReturn(true);
        when(exerciseProvider.next()).thenReturn(anyExcercise);

        new ExamServiceImpl(5, 3, exerciseProvider,  authService, userInterface).doExam();
        verify(exerciseProvider, times(5)).next();
        verify(exerciseProvider, times(1)).close();
        verify(userInterface, times(1)).printExamPassed(any(), any(Integer.class));
    }

    @Test
    public void doExamFail() {
        Excercise anyExcercise = mock(Excercise.class);
        when(anyExcercise.getRightAnswers()).thenReturn(Stream.of("myAnswer").collect(toSet()));

        when(exerciseProvider.hasNext()).thenReturn(true);
        when(exerciseProvider.next()).thenReturn(anyExcercise);

        new ExamServiceImpl(5, 3, exerciseProvider, authService, userInterface).doExam();
        verify(userInterface, times(1)).printExamFailed(any(), any(Integer.class));
    }
}