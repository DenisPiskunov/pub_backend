package ru.mint.mobile.store.parser.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.mobile.store.parser.repository.AppKeyGamblingRepository
import ru.mint.mobile.store.parser.repository.ExcludedWordsRepository
import ru.mint.mobile.store.parser.repository.entity.AppKeyGambling
import ru.mint.mobile.store.parser.repository.entity.ExcludedWords
import ru.mint.mobile.store.parser.rest.domain.KeywordsBundle
import ru.mint.mobile.store.parser.service.KeywordsService
import ru.mint.mobile.store.parser.service.dto.EditMode
import ru.mint.mobile.store.parser.service.dto.WordsType

@Service
class KeywordsServiceImpl : KeywordsService {

    @Autowired
    private lateinit var appKeyGamblingRepository: AppKeyGamblingRepository
    @Autowired
    private lateinit var excludedWordsRepository: ExcludedWordsRepository

    override fun getKeywords(type: String): List<String> {
        val wordsType: WordsType = try {
            WordsType.valueOf(type)
        }
        catch (e: Exception){
            return mutableListOf()
        }
        return when (wordsType) {
            WordsType.GAMBLING -> appKeyGamblingRepository.findAll().map { (it.key) }
            WordsType.EXCLUDED -> excludedWordsRepository.findAll().map { (it.key) }
        }
    }

    override fun editKeywords(keywordsBundle: KeywordsBundle): Boolean {
        val wordList = keywordsBundle.wordList
        val wordsType: WordsType = try {
            WordsType.valueOf(keywordsBundle.wordsType)
        }
        catch (e: Exception){
            return false
        }

        val editMode: EditMode = try {
            EditMode.valueOf(keywordsBundle.editMode)
        }
        catch (e: Exception){
            return false
        }



        if (wordsType == WordsType.GAMBLING && wordList.count() > 0) {
            when (editMode) {
                EditMode.ADD -> {
                    wordList.forEach { word ->
                        if (!appKeyGamblingRepository.existsByKey(word)) {
                            appKeyGamblingRepository.save(AppKeyGambling(word))
                        }
                    }
                }
                EditMode.DELETE -> {
                    wordList.forEach { word ->
                        val id = appKeyGamblingRepository.findOneByKey(word).id
                        appKeyGamblingRepository.delete(id)
                    }
                }
            }
            return true
        }

        if (wordsType == WordsType.EXCLUDED && wordList.count() > 0) {
            when (editMode) {
                EditMode.ADD -> {
                    wordList.forEach { word ->
                        if (!excludedWordsRepository.existsByKey(word)) {
                            excludedWordsRepository.save(ExcludedWords(word))
                        }
                    }
                }
                EditMode.DELETE -> {
                    wordList.forEach { word ->
                        val id = excludedWordsRepository.findOneByKey(word).id
                        excludedWordsRepository.delete(id)
                    }
                }
            }
            return true
        }

        return false
    }
}