import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseViewPreviewComponent } from './course-view-preview.component';

describe('CourseViewPreviewComponent', () => {
  let component: CourseViewPreviewComponent;
  let fixture: ComponentFixture<CourseViewPreviewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CourseViewPreviewComponent]
    });
    fixture = TestBed.createComponent(CourseViewPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
