export interface Repository {

    getItem(key: string): string;

    setItem(key: string, value: string);

    removeItem(key: string);

    clear();
}

export enum RepositoryType { SESSION, LOCAL, IN_MEMORY}

export class InMemoryRepository implements Repository {

    private readonly map: Map<string, string>;

    constructor() {
        this.map = new Map<string, string>();
    }

    public getItem(key: string): string {
        return this.map.get(key);
    }

    public setItem(key: string, value: string) {
        this.map.set(key, value);
    }

    public removeItem(key: string) {
        this.map.delete(key);
    }

    public clear() {
        this.map.clear();
    }
}

export class LocalRepository implements Repository {

    getItem(key: string): string {
        return localStorage.getItem(key);
    }

    setItem(key: string, value: string) {
        localStorage.setItem(key, value);
    }

    removeItem(key: string) {
        localStorage.removeItem(key);
    }

    clear() {
        localStorage.clear();
    }
}

export class SessionRepository implements Repository {

    getItem(key: string): string {
        return sessionStorage.getItem(key);
    }

    setItem(key: string, value: string) {
        sessionStorage.setItem(key, value);
    }

    removeItem(key: string) {
        sessionStorage.removeItem(key);
    }

    clear() {
        sessionStorage.clear();
    }
}
