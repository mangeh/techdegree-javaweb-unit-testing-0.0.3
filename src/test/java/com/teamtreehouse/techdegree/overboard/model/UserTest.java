package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import com.teamtreehouse.techdegree.overboard.model.Answer;
import com.teamtreehouse.techdegree.overboard.model.Board;
import com.teamtreehouse.techdegree.overboard.model.Question;
import com.teamtreehouse.techdegree.overboard.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Magnus on 2016-11-02.
 */
public class UserTest {
    Board board;
    User author;
    User answerer;
    Question question;
    Answer answer;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        board = new Board("Tests");
        author = new User(board, "QuestionPerson");
        answerer = new User(board, "AnswerPerson");
        question = new Question(author, "How do i test?");
        answer = new Answer(question, answerer, "Just try!");
        board.addQuestion(question);
        board.addAnswer(answer);
    }

    @Test
    public void isReputationIncreasedByUpvoteOnQuestion() throws Exception {
        int reputationBefore = author.getReputation();

        for (Question q : board.getQuestions()) {
            if (q.equals(question)) {
                q.addUpVoter(answerer);
            }
        }

        assertEquals(reputationBefore + 5, author.getReputation());
    }

    @Test
    public void isReputationIncreasedByUpvoteOnAnswer() throws Exception {
        int reputationBefore = answerer.getReputation();

        for (Answer a : board.getAnswers()) {
            if (a.equals(answer)) {
                a.addUpVoter(author);
            }
        }

        assertEquals(reputationBefore + 10, answerer.getReputation());
    }

    @Test
    public void isReputationIncreasedByAcceptingnAnswer() throws Exception {
        int reputationBefore = answerer.getReputation();

        for (Answer a : board.getAnswers()) {
            if (a.equals(answer)) {
                a.setAccepted(true);
            }
        }

        assertEquals(reputationBefore + 15, answerer.getReputation());
    }

    @Test
    public void authorCanNotUpvoteQestion() throws Exception {
        expectedException.expect(VotingException.class);
        expectedException.expectMessage("You cannot vote for yourself!");

        author.upVote(question);
    }

    @Test
    public void onlyQuestionAuthorCanAcceptAnswer() throws Exception {
        expectedException.expect(AnswerAcceptanceException.class);
        expectedException.expectMessage("Only " + author.getName() + " can accept this answer as it is their question");

        answerer.acceptAnswer(answer);
    }


}

