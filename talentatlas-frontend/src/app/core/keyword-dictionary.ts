import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, shareReplay } from 'rxjs';

export interface KeywordDictionaryEntry {
  name: string;
  category: string; 
  aliases: string[];
}

@Injectable({ providedIn: 'root' })
export class KeywordDictionaryService {
  private entries$: Observable<KeywordDictionaryEntry[]>;

  constructor(private http: HttpClient) {
    this.entries$ = this.http
  .get<KeywordDictionaryEntry[]>('keyword-dictionary.json')
  .pipe(shareReplay(1));
  }

  getEntries(): Observable<KeywordDictionaryEntry[]> {
    return this.entries$;
  }

  search(term: string): Observable<KeywordDictionaryEntry[]> {
    const query = term.trim().toLowerCase();
    if (!query) {
      return this.entries$.pipe(map(() => []));
    }

    return this.entries$.pipe(
      map(entries =>
        entries.filter(entry => {
          const name = entry.name.toLowerCase();
          if (name.includes(query)) return true;
          return entry.aliases.some(a => a.toLowerCase().includes(query));
        }).slice(0, 8)
      )
    );
  }
}
