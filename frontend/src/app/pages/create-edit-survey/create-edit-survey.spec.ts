import { ComponentFixture, TestBed } from '@angular/core/testing';

import CreateEditSurvey from './create-edit-survey';

describe('CreateEditSurvey', () => {
  let component: CreateEditSurvey;
  let fixture: ComponentFixture<CreateEditSurvey>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateEditSurvey]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditSurvey);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
