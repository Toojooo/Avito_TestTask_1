package org.example.avito_testtask_1.factory;

import org.example.avito_testtask_1.model.Pr;
import org.example.avito_testtask_1.model.User;
import org.example.avito_testtask_1.model.enums.PrStatus;

import java.util.HashSet;
import java.util.Set;

public class PrFactory {
    public static Pr create(String title, User author) {
        Pr pr = new Pr();
        pr.setTitle(title);
        pr.setAuthor(author);
        pr.setStatus(PrStatus.OPEN);
        pr.setReviewers(new HashSet<>());
        return pr;
    }
    public static Pr createWithReviewers(String title, User author, Set<User> reviewers) {
        Pr pr = create(title, author);
        pr.setReviewers(reviewers);
        return pr;
    }
}
