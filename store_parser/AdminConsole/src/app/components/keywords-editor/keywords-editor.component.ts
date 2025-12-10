import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {KeywordsBundle} from '../../domain/keywords-bundle';
import {WordsType} from '../../domain/words-type';
import {EditMode} from '../../domain/edit-mode';
import {KeywordsService} from '../../services/keywords-service';

@Component({
    selector: 'app-keywords-editor',
    templateUrl: './keywords-editor.component.html',
    styleUrls: ['./keywords-editor.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class KeywordsEditorComponent implements OnInit {
    appGamblingKeys: string[] = [];
    excludedWords: string[] = [];

    removedGamblingKeys: string[] = [];
    removedExcludedWords: string[] = [];

    addedGamblingKeys: string[] = [];
    addedExcludedWords: string[] = [];

    blockedDocument = false;
    hideProgress = true;

    gamblingListChanged = false;
    excludedWordsListChanged = false;

    constructor(private keywordService: KeywordsService) {
    }

    ngOnInit() {
        this.getWordLists();
    }

    getWordLists() {
        this.blockedDocument = true;
        this.hideProgress = false;
        this.keywordService.getKeywords(WordsType.GAMBLING).then(data => {
            this.blockedDocument = false;
            this.hideProgress = true;
            this.appGamblingKeys = data;
        });

        this.keywordService.getKeywords(WordsType.EXCLUDED).then(data => {
            this.excludedWords = data;
            this.blockedDocument = false;
            this.hideProgress = true;
        });
    }

    onAddGamblingKey(key: string) {
        this.addedGamblingKeys.push(key);
        this.gamblingListChanged = true;
    }

    onAddExcludedWord(key: string) {
        this.addedExcludedWords.push(key);
        this.excludedWordsListChanged = true;
    }

    onRemoveGamblingKey(key: string) {
        this.removedGamblingKeys.push(key);
        this.gamblingListChanged = true;
    }

    onRemoveExcludedWord(key: string) {
        this.removedExcludedWords.push(key);
        this.excludedWordsListChanged = true;
    }

    onRevertGamblingKeys() {
        this.removedGamblingKeys.forEach(
            value => {
                this.appGamblingKeys.push(value);
            }
        );
        this.removedGamblingKeys = [];

        this.addedGamblingKeys.forEach(
            value => {
                const index = this.appGamblingKeys.indexOf(value);
                this.appGamblingKeys.splice(index, 1);
            }
        );
        this.addedGamblingKeys = [];
        this.gamblingListChanged = false;
    }


    onRevertExcludedWords() {
        this.removedExcludedWords.forEach(
            value => {
                this.excludedWords.push(value);
            }
        );
        this.removedExcludedWords = [];

        this.addedExcludedWords.forEach(
            value => {
                const index = this.excludedWords.indexOf(value);
                this.excludedWords.splice(index, 1);
            }
        );
        this.addedExcludedWords = [];
        this.excludedWordsListChanged = false;
    }

    onSaveChangesGamblingKeys() {
        if (
            this.addedGamblingKeys.length > 0 ||
            this.removedGamblingKeys.length > 0 ||
            this.addedExcludedWords.length > 0 ||
            this.removedExcludedWords.length > 0) {
            this.blockedDocument = true;
            this.hideProgress = false;
        }

        if (this.addedGamblingKeys.length > 0) {
            const addedBundle: KeywordsBundle = {
                wordList: this.addedGamblingKeys,
                wordsType: WordsType[WordsType.GAMBLING],
                editMode: EditMode[EditMode.ADD]
            };
            this.keywordService.editKeywords(addedBundle)
                .then(response => {
                    if (response) {
                        this.addedGamblingKeys = [];
                    }
                    this.blockedDocument = false;
                    this.hideProgress = true;
                    this.gamblingListChanged = false;
                });
        }

        if (this.removedGamblingKeys.length > 0) {
            const removedBundle: KeywordsBundle = {
                wordList: this.removedGamblingKeys,
                wordsType: WordsType[WordsType.GAMBLING],
                editMode: EditMode[EditMode.DELETE]
            };
            this.keywordService.editKeywords(removedBundle)
                .then(response => {
                    if (response) {
                        this.removedGamblingKeys = [];
                    }
                    this.blockedDocument = false;
                    this.hideProgress = true;
                    this.gamblingListChanged = false;
                });
        }
    }

    onSaveChangesExcludedWords() {
        if (this.addedExcludedWords.length > 0) {
            const addedBundle: KeywordsBundle = {
                wordList: this.addedExcludedWords,
                wordsType: WordsType[WordsType.EXCLUDED],
                editMode: EditMode[EditMode.ADD]
            };
            this.keywordService.editKeywords(addedBundle)
                .then(response => {
                    if (response) {
                        this.addedExcludedWords = [];
                    }
                    this.blockedDocument = false;
                    this.hideProgress = true;
                    this.excludedWordsListChanged = false;
                });

        }

        if (this.removedExcludedWords.length > 0) {
            const removedBundle: KeywordsBundle = {
                wordList: this.removedExcludedWords,
                wordsType: WordsType[WordsType.EXCLUDED],
                editMode: EditMode[EditMode.DELETE]
            };
            this.keywordService.editKeywords(removedBundle)
                .then(response => {
                    if (response) {
                        this.removedExcludedWords = [];
                    }
                    this.blockedDocument = false;
                    this.hideProgress = true;
                    this.excludedWordsListChanged = false;
                });
        }
    }


}
