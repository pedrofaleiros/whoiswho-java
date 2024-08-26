package dev.pedrofaleiros.whoiswho_api.service;

import dev.pedrofaleiros.whoiswho_api.entity.GameCategory;

public interface GameCategoryService {
    GameCategory findById(String id);
}
