import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {KEYWORDS_EDIT_URL, KEYWORDS_GET_URL} from '../support/constants';
import {KeywordsBundle} from '../domain/keywords-bundle';
import {BaseHttpService} from './base-http-service';
import {WordsType} from '../domain/words-type';
import {Utils} from '../support/utils';
import {ConfigService} from './config-service';

@Injectable()
export class KeywordsService extends BaseHttpService {

    constructor(private http: Http, private utils: Utils, private configService: ConfigService) {
        super();
    }

    editKeywords(bundle: KeywordsBundle) {
        return this.http.post(this.configService.getUrl(KEYWORDS_EDIT_URL), JSON.stringify(bundle), {headers: this.header})
            .toPromise()
            .catch(this.handleError);
    }

    getKeywords(wordsType: WordsType) {
        return this.http.get(this.utils.strFormat([this.configService.getUrl(KEYWORDS_GET_URL), WordsType[wordsType]]),
            {headers: this.header})
            .toPromise()
            .then(response => response.json() as string[])
            .catch(this.handleError);
    }
}
