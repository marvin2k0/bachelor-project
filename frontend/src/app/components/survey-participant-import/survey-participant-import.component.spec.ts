import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SurveyParticipantImportComponent } from './survey-participant-import.component';

describe('SurveyParticipantImportComponent', () => {
  let component: SurveyParticipantImportComponent;
  let fixture: ComponentFixture<SurveyParticipantImportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SurveyParticipantImportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SurveyParticipantImportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
