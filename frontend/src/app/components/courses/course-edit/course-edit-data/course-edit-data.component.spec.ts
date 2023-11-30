import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseEditDataComponent } from './course-edit-data.component';

describe('CourseEditDataComponent', () => {
  let component: CourseEditDataComponent;
  let fixture: ComponentFixture<CourseEditDataComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CourseEditDataComponent]
    });
    fixture = TestBed.createComponent(CourseEditDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
