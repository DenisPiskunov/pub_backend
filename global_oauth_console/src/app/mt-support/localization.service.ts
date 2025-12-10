import {EventEmitter, Injectable} from '@angular/core';
import {ApplicationRepository} from '../mt-repository/application-repository';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {RepositoryKey} from '../mt-repository/repository-key';
import {RepositoryType} from '../mt-repository/repository';

@Injectable({
    providedIn: 'root'
})
export class LocalizationService {

    readonly languages = [
        {
            label: 'English',
            value: 'en'
        },
        {
            label: 'Русский',
            value: 'ru'
        }
    ];

    // tslint:disable-next-line:variable-name
    private _currentLang: string;

    constructor(private applicationRepository: ApplicationRepository, private translate: TranslateService) {
        this.currentLang = this.getDefaultLang();
    }

    get currentLang(): string {
        return this._currentLang;
    }

    set currentLang(value: string) {
        if (this.isLangAvailable(value)) {
            this._currentLang = value;
            this.translate.use(this._currentLang);
            this.applicationRepository.setItem(RepositoryKey.LANGUAGE, this._currentLang, RepositoryType.LOCAL);
        }
    }

    getTranslation(key: string): string | any {
        return this.translate.instant(key);
    }

    onLangChangeEvent(): EventEmitter<LangChangeEvent> {
        return this.translate.onLangChange;
    }

    private getDefaultLang(): string {
        const chosenLang = this.applicationRepository.getItem(RepositoryKey.LANGUAGE, RepositoryType.LOCAL);
        if (chosenLang) {
            return chosenLang;
        } else {
            const browserLang = this.translate.getBrowserLang();
            return this.isLangAvailable(browserLang) ? browserLang : this.translate.getDefaultLang();
        }
    }

    private isLangAvailable(lang: string): boolean {
        return this.languages.some(language => language.value === lang);
    }
}
