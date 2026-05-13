import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KeywordAnalysis } from './keyword-analysis';

describe('KeywordAnalysis', () => {
  let component: KeywordAnalysis;
  let fixture: ComponentFixture<KeywordAnalysis>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [KeywordAnalysis],
    }).compileComponents();

    fixture = TestBed.createComponent(KeywordAnalysis);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
