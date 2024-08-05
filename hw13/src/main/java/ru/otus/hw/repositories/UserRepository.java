package ru.otus.hw.repositories;

import ru.otus.hw.models.User;

public interface UserRepository {
    User findByUsername(String username);
}
