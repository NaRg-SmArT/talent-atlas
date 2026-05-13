import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSkill } from './add-skill';

describe('AddSkill', () => {
  let component: AddSkill;
  let fixture: ComponentFixture<AddSkill>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSkill],
    }).compileComponents();

    fixture = TestBed.createComponent(AddSkill);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
