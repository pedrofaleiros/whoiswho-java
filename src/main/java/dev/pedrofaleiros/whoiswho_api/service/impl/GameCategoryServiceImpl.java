package dev.pedrofaleiros.whoiswho_api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.pedrofaleiros.whoiswho_api.entity.GameCategory;
import dev.pedrofaleiros.whoiswho_api.exception.not_found.GameCategoryNotFoundException;
import dev.pedrofaleiros.whoiswho_api.repository.GameCategoryRepository;
import dev.pedrofaleiros.whoiswho_api.service.GameCategoryService;

@Service
public class GameCategoryServiceImpl implements GameCategoryService {

    @Autowired
    private GameCategoryRepository repository;

    @Override
    public GameCategory findById(String id) {
        return repository.findById(id).orElseThrow(() -> new GameCategoryNotFoundException());
    }

}
