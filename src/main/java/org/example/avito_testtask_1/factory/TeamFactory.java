package org.example.avito_testtask_1.factory;

import org.example.avito_testtask_1.model.Team;
import org.example.avito_testtask_1.model.User;

import java.util.ArrayList;
import java.util.List;

public class TeamFactory {
    public static Team create(String name) {
        Team team = new Team();
        team.setName(name);
        team.setUsers(new ArrayList<>());
        return team;
    }
    public static Team createWithUsers(String name, List<User> users) {
        Team team = create(name);
        team.setUsers(users);

        users.forEach(user -> user.setTeam(team));

        return team;
    }
}
