import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndexUnauthorizedComponent } from './index-unauthorized.component';

describe('IndexUnauthorizedComponent', () => {
  let component: IndexUnauthorizedComponent;
  let fixture: ComponentFixture<IndexUnauthorizedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IndexUnauthorizedComponent]
    });
    fixture = TestBed.createComponent(IndexUnauthorizedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
