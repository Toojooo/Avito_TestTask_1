package org.example.avito_testtask_1.factory;

import org.example.avito_testtask_1.model.Team;
import org.example.avito_testtask_1.model.User;

public class UserFactory {
    public static User create(String name, Team team) {
        User user = new User();
        user.setName(name);
        user.setActive(true);
        user.setTeam(team);

        if (team != null) {
            team.getUsers().add(user);
        }

        return user;
    }

    public static User createInactive(String name, Team team) {
        User user = create(name, team);
        user.setActive(false);
        return user;
    }
}

