import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseViewVideoComponent } from './course-view-video.component';

describe('CourseViewVideoComponent', () => {
  let component: CourseViewVideoComponent;
  let fixture: ComponentFixture<CourseViewVideoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CourseViewVideoComponent]
    });
    fixture = TestBed.createComponent(CourseViewVideoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
