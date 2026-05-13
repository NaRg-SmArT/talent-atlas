import { TestBed } from '@angular/core/testing';

import { KeywordDictionaryService } from './keyword-dictionary';

describe('KeywordDictionary', () => {
  let service: KeywordDictionaryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(KeywordDictionaryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
