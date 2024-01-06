import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseMyCoursesComponent } from './course-my-courses.component';

describe('CourseMyCoursesComponent', () => {
  let component: CourseMyCoursesComponent;
  let fixture: ComponentFixture<CourseMyCoursesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CourseMyCoursesComponent]
    });
    fixture = TestBed.createComponent(CourseMyCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
