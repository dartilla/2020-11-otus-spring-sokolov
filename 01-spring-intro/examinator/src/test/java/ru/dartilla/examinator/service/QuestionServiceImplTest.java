package ru.dartilla.examinator.service;

import org.junit.Assert;
import org.junit.Test;
import ru.dartilla.examinator.domain.Question;

public class QuestionServiceImplTest {

    @Test
    public void parseLine() {
        QuestionServiceImpl questionService = new QuestionServiceImpl("");
        Question question = questionService.parseLine("Вода бывает в виде...,1-2-3,Жидкая,Газообразная,Твердая");
        Assert.assertEquals("Вода бывает в виде...", question.getContent());
        Assert.assertEquals(3, question.getAnswersToChoose().size());
        Assert.assertEquals(3, question.getRightAnswers().size());

        question = questionService.parseLine("Вместе весело шагать по...,1,просторам");
        Assert.assertEquals("Вместе весело шагать по...", question.getContent());
        Assert.assertEquals(0, question.getAnswersToChoose().size());
        Assert.assertEquals("просторам", question.getRightAnswers().iterator().next().getContent());
    }

    @Test
    public void getQuestions() {
        Assert.assertEquals(5, new QuestionServiceImpl("/questionsAndAnswers.csv").getQuestions().size());
    }
}