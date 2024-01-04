import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseViewOutlookComponent } from './course-view-outlook.component';

describe('CourseViewOutlookComponent', () => {
  let component: CourseViewOutlookComponent;
  let fixture: ComponentFixture<CourseViewOutlookComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CourseViewOutlookComponent]
    });
    fixture = TestBed.createComponent(CourseViewOutlookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
