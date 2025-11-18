import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupDisplay } from './group-display';

describe('GroupDisplay', () => {
  let component: GroupDisplay;
  let fixture: ComponentFixture<GroupDisplay>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupDisplay]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GroupDisplay);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
