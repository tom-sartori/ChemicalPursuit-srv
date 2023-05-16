package chemistry.death.race.service;

import chemistry.death.race.collection.question.Proposition;
import chemistry.death.race.collection.question.Question;
import chemistry.death.race.constant.Constants;
import chemistry.death.race.repository.QuestionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;

@ApplicationScoped
public class QuestionService {

    @Inject
    QuestionRepository questionRepository;

    public boolean isValid(Question question) {
        ifNullThrowBadRequestException(question.getName(), "Name is required. ");
        if (!isCategoryValid(question.getDifficulty(), question.getCategory())) {
            throw new BadRequestException("Category is not valid. ");
        }
        if (question.getPropositions().size() < 1 || Constants.MAX_NUMBER_OF_PROPOSITION < question.getPropositions().size()) {
            throw new BadRequestException("Number of propositions must be between 1 and 4. ");
        }
        if (question.getPropositions().stream().filter(Proposition::isAnswer).count() != 1) {
            throw new BadRequestException("There must be exactly one correct proposition. ");
        }

        return true;
    }

    private void ifNullThrowBadRequestException(String value, String message) {
        if (value == null || value.isEmpty()) {
            throw new BadRequestException(message);
        }
    }

    private boolean isCategoryValid(String difficulty, String category) {
        ifNullThrowBadRequestException(category, "Category is required. ");

        List<String> allCategories = questionRepository.findAllCategoriesByDifficulty(difficulty);
        if (allCategories.size() < Constants.MAX_NUMBER_OF_CATEGORY) {
            return true;
        }
        else {
            return allCategories.contains(category);
        }
    }
}