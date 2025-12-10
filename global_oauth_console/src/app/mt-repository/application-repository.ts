import {Injectable} from '@angular/core';
import {RepositoryKey} from './repository-key';
import {InMemoryRepository, LocalRepository, Repository, RepositoryType, SessionRepository} from './repository';

@Injectable({
    providedIn: 'root'
})
export class ApplicationRepository {

    private readonly repositoriesByType: Map<RepositoryType, Repository>;

    constructor() {
        this.repositoriesByType = new Map([
            [RepositoryType.IN_MEMORY, new InMemoryRepository()],
            [RepositoryType.SESSION, new SessionRepository()],
            [RepositoryType.LOCAL, new LocalRepository()]
        ]);
    }

    getItem(key: RepositoryKey, type: RepositoryType): string {
        return this.repositoriesByType.get(type).getItem(key);
    }

    setItem(key: RepositoryKey, value: string, type: RepositoryType) {
        this.repositoriesByType.get(type).setItem(key, value);
    }

    removeItem(key: RepositoryKey, type: RepositoryType) {
        this.repositoriesByType.get(type).removeItem(key);
    }

    clear(type?: RepositoryType) {
        if (type) {
            this.repositoriesByType.get(type).clear();
        } else {
            for (const key of this.repositoriesByType.keys()) {
                this.repositoriesByType.get[key].clear();
            }
        }
    }
}
