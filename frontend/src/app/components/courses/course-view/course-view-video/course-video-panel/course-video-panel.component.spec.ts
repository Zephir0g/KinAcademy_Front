import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseVideoPanelComponent } from './course-video-panel.component';

describe('CourseVideoPanelComponent', () => {
  let component: CourseVideoPanelComponent;
  let fixture: ComponentFixture<CourseVideoPanelComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CourseVideoPanelComponent]
    });
    fixture = TestBed.createComponent(CourseVideoPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
